# Aion — Codebase Analysis & Improvement Plan

---

## Architecture Decision: Offline / Online Dual-Mode

The app will support **two modes** selectable during onboarding and switchable anytime via Settings:

| | Offline Mode | Online Mode |
|--|-------------|-------------|
| **Account** | Generic local profile (name + icon, no auth) | Firebase Auth (Email/Password or Google Sign-In) |
| **Data** | Room DB only — fully local | Room DB + Firestore sync for gamification data |
| **Features** | Usage tracking, limits, notifications, score, streaks, theme/accent | Everything in offline **+** Resistance Points, achievements, shop/arsenal, leaderboards |
| **Tabs** | Home, Plan, Notifications, Settings | Home, Plan, Notifications, Settings (same nav — gamification woven into existing screens) |
| **Profile** | Local display name, default avatar, rank based on score | Full profile with RP balance, achievements, global rank, avatar picker |

### Why this split?
1. **Privacy-first users** get a fully functional, zero-signup experience.
2. **Engaged users** opt into gamification and social features when ready.
3. **Development priority** — offline mode is 90% done. Fix bugs, polish, ship. Online mode layers on top without breaking the core.

### Data Strategy
- **Room remains the single source of truth** for both modes.
- **Firestore mirrors gamification tables** (`user_points`, `points_transactions`, `achievements`, `user_achievements`, `shop_items`, `user_inventory`) for online users only.
- **Sync direction:** Room → Firestore on write; Firestore → Room on login/device-switch.
- **Mode switch:** Offline → Online: create Firebase account, upload local data. Online → Offline: keep local data, stop syncing.

---

## Unified Scoring & Currency System

> Replaces the current weak `ScoreUtils.calculateScore()` (`100 * (1 - ratio)`) with a modular, multi-currency system inspired by Duolingo (XP + streaks), Habitica (gold/damage), and Forest (visual progress).

### Design Principles
1. **Score reflects effort, not just outcome** — staying 10 min under limit is worth more than staying 1 min under.
2. **Penalty is proportional, never crushing** — overshoot hurts, but one bad day doesn't wipe out a week of progress.
3. **Streaks amplify everything** — consistency is the #1 behavior we reward.
4. **Currencies separate "status" from "spending"** — you can't buy your way to a high rank.
5. **Offline users get the full scoring experience** — currencies that require online are additive, not essential.

### Currency Table

| Currency | Symbol | Mode | Purpose | Earned By | Spent On |
|----------|--------|------|---------|-----------|----------|
| **Score** | 📊 | Both | Performance rating per-app and global (0–100) | Computed from usage vs limits | Nothing — it's a gauge, not a spendable currency |
| **XP** | ⭐ | Both | Leveling + rank progression | Completing days under limit, streaks, achievements | Nothing — it's cumulative and never decreases |
| **Gold** | 🪙 | Online | Soft currency for the shop | Under-limit check-ins, achievements, daily login | Shop items (quotes, themes, avatars) |
| **Gems** | 💎 | Online | Premium/rare currency | Major achievements, long streaks (7+), perfect days | Rare shop items, streak freezes (future) |

> **Offline mode** uses Score + XP only. Gold and Gems are hidden until the user switches to online.
> **XP never resets.** It's your lifetime progress. Score resets daily (it's today's performance snapshot).

---

### Per-App Credit Score (0–100)

Computed daily for each tracked app. This replaces the current `ScoreUtils.calculateScore()`.

```
AppScore = BaseScore × StreakMultiplier − OvershootPenalty
```

#### BaseScore (0–100)

```kotlin
fun calculateBaseScore(usageMs: Long, limitMs: Long): Float {
    if (limitMs <= 0L) return 0f         // No limit set → no score
    if (usageMs <= 0L) return 100f       // Zero usage → perfect score

    val ratio = usageMs.toFloat() / limitMs
    return when {
        ratio <= 0.5f -> 100f                           // Used ≤50% of limit → perfect
        ratio <= 1.0f -> 100f - (ratio - 0.5f) * 100f  // 50–100% → linear drop 100→50
        else -> 50f * (1f / ratio)                      // Over limit → decays toward 0
    }.coerceIn(0f, 100f)
}
```

**Why this curve?**
- Under 50% usage → full marks (you're well under control).
- 50–100% → gradual decline (you're approaching danger).
- Over 100% → steep drop but never exactly 0 (encourages recovery, not giving up).

#### StreakMultiplier

```kotlin
fun getStreakMultiplier(consecutiveDaysUnderLimit: Int): Float {
    return when {
        consecutiveDaysUnderLimit >= 30 -> 1.5f
        consecutiveDaysUnderLimit >= 14 -> 1.35f
        consecutiveDaysUnderLimit >= 7  -> 1.2f
        consecutiveDaysUnderLimit >= 3  -> 1.1f
        else -> 1.0f
    }
}
```

Streak multiplier **boosts good days** but is capped at 1.5× to prevent runaway inflation.

#### OvershootPenalty

Applied only when `usage > limit`:

```kotlin
fun calculateOvershootPenalty(usageMs: Long, limitMs: Long): Float {
    if (limitMs <= 0L || usageMs <= limitMs) return 0f
    val overshootRatio = (usageMs - limitMs).toFloat() / limitMs
    return (overshootRatio * 30f).coerceAtMost(40f)  // Max 40 point penalty
}
```

- 10% over limit → -3 points.
- 50% over limit → -15 points.
- 100%+ over limit → capped at -40 points.

#### Final Per-App Score

```kotlin
fun calculateAppScore(usageMs: Long, limitMs: Long, streakDays: Int): Float {
    val base = calculateBaseScore(usageMs, limitMs)
    val multiplier = getStreakMultiplier(streakDays)
    val penalty = calculateOvershootPenalty(usageMs, limitMs)
    return (base * multiplier - penalty).coerceIn(0f, 100f)
}
```

---

### Global Composite Score (0–100)

The user's overall daily performance across all tracked apps.

```kotlin
fun calculateGlobalScore(appScores: List<Pair<Float, Long>>): Float {
    // appScores = list of (appScore, limitMs) pairs
    // Weight each app by its limit — apps with stricter limits matter more
    val totalWeight = appScores.sumOf { it.second }
    if (totalWeight <= 0L) return 0f

    return appScores.sumOf { (score, limit) ->
        (score * limit / totalWeight.toFloat()).toDouble()
    }.toFloat().coerceIn(0f, 100f)
}
```

**Why weighted?** If Instagram has a 30-min limit and a casual game has a 4-hour limit, Instagram performance should weigh more — it's the harder goal.

---

### XP System (Leveling & Rank)

XP is **cumulative and never decreases**. It represents lifetime effort.

#### XP Earning Table

| Action | XP Earned | Mode | Notes |
|--------|-----------|------|-------|
| Day completed under limit (per app) | `10 + floor(timeSavedMinutes / 5)` | Both | More time saved = more XP |
| Day completed under limit (global — all apps) | `25` | Both | Flat bonus for keeping ALL apps under |
| Streak milestone (3 days) | `50` | Both | One-time per streak |
| Streak milestone (7 days) | `150` | Both | One-time per streak |
| Streak milestone (14 days) | `400` | Both | One-time per streak |
| Streak milestone (30 days) | `1000` | Both | One-time per streak |
| Achievement unlocked | Achievement's `pointsReward` | Online | Defined per achievement |
| Perfect day (Score ≥ 95 across all apps) | `50` | Both | Encourages excellent behavior |

#### XP → Level Formula

```kotlin
fun levelForXp(totalXp: Int): Int {
    // Each level requires progressively more XP
    // Level 1 = 0 XP, Level 2 = 100, Level 3 = 250, Level 4 = 450 ...
    // Formula: xpForLevel(n) = 50 * n * (n - 1) / 2 → triangular growth
    var level = 1
    var xpNeeded = 0
    while (totalXp >= xpNeeded) {
        level++
        xpNeeded += 50 * level
    }
    return level - 1
}

fun xpForNextLevel(currentLevel: Int): Int {
    return 50 * (currentLevel + 1)
}
```

Growth curve: Level 1→2 takes 100 XP. Level 10→11 takes 550 XP. Level 20→21 takes 1050 XP. Feels achievable early, becomes a badge of commitment later.

#### Rank Ladder

| Level Range | Rank | Badge |
|-------------|------|-------|
| 1–4 | Recruit | 🔰 |
| 5–9 | Operative | 🛡️ |
| 10–14 | Agent | ⚔️ |
| 15–19 | Commander | 🎖️ |
| 20–29 | Elite | 💠 |
| 30–39 | Legend | 🏆 |
| 40+ | Mythic | 👑 |

> This replaces the current simplistic score-based rank (`Beginner/Intermediate/Pro/Expert/Legend`). Rank is now driven by **cumulative XP** (lifetime effort), not today's score.

---

### Gold & Gems (Online Only)

#### Gold 🪙

Earned frequently, spent on common shop items.

| Action | Gold Earned | Notes |
|--------|-------------|-------|
| Under-limit check-in (per worker run, ~every 15 min) | `5` | Only if total usage < total limit |
| Day completed under limit (per app) | `20 + floor(timeSavedMinutes / 10)` | More saved time = more gold |
| Achievement unlocked | `25–100` (varies by achievement) | Defined per achievement |
| Daily login (open app while under limit) | `10` | Once per day |

#### Gems 💎

Earned rarely, spent on premium shop items and future streak freezes.

| Action | Gems Earned | Notes |
|--------|-------------|-------|
| 7-day streak reached | `5` | One-time per streak |
| 14-day streak reached | `15` | One-time per streak |
| 30-day streak reached | `50` | One-time per streak |
| Perfect day (Score ≥ 95, all apps under limit) | `3` | Max once per day |
| Achievement: Elite Status (score ≥ 90) | `10` | One-time |
| Achievement: Monthly Legend | `25` | One-time |

---

### Streak System (Unified)

Currently per-app streaks exist in `AppDetailsScreen` but are isolated. The new system unifies them:

#### Per-App Streak
- **Increments:** At end of day, if `appUsage ≤ appLimit` for the day.
- **Resets:** If `appUsage > appLimit` for the day.
- **Displayed on:** App Details overview tab.

#### Global Streak
- **Increments:** At end of day, if **ALL tracked apps** are under their limits.
- **Resets:** If **any** tracked app exceeds its limit.
- **Displayed on:** Home screen, Profile.
- **Drives:** XP milestones, Gem rewards, achievement conditions, streak multiplier.

#### Streak Freeze (Online only, future)
- Costs 10 Gems.
- Preserves streak for 1 missed day.
- Max 1 per week.

---

### How It All Connects

```
                    ┌─────────────┐
                    │  UsageWorker │  (runs every 15 min)
                    └──────┬──────┘
                           │
              ┌────────────▼────────────┐
              │  For each tracked app:  │
              │  - Query usage vs limit │
              │  - Calculate AppScore   │
              │  - Update streak count  │
              └────────────┬────────────┘
                           │
              ┌────────────▼────────────┐
              │  Compute global metrics │
              │  - GlobalScore          │
              │  - GlobalStreak         │
              │  - XP earned            │
              │  - Gold earned (online) │
              │  - Gems earned (online) │
              └────────────┬────────────┘
                           │
              ┌────────────▼────────────┐
              │  Check achievements     │
              │  (online mode only)     │
              │  - Award XP/Gold/Gems   │
              │  - Fire notifications   │
              └────────────┬────────────┘
                           │
              ┌────────────▼────────────┐
              │  Write to Room DB       │
              │  + sync to Firestore    │
              │  (if online mode)       │
              └─────────────────────────┘
```

### DB Schema Changes Required

The existing `UserPointsEntity` needs to expand from a single `balance` field:

```kotlin
@Entity(tableName = "user_points")
data class UserPointsEntity(
    @PrimaryKey val userId: String = "default_user",
    val xp: Int = 0,           // Cumulative, never decreases
    val gold: Int = 0,         // Spendable (online only)
    val gems: Int = 0,         // Rare spendable (online only)
    val globalStreak: Int = 0, // Current global streak days
    val longestStreak: Int = 0 // All-time best streak
)
```

`PointsTransactionEntity` already has `amount` + `reason` — add a `currency` field:

```kotlin
@Entity(tableName = "points_transactions")
data class PointsTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val currency: String,  // "XP", "GOLD", "GEMS"
    val amount: Int,
    val reason: String,
    val timestamp: Long = System.currentTimeMillis()
)
```

> **Migration note:** This changes `UserPointsEntity` (rename `balance` → `xp`, add `gold`, `gems`, `globalStreak`, `longestStreak`) and adds `currency` to `PointsTransactionEntity`. Since we use `fallbackToDestructiveMigration`, this is safe during development. For production, a proper Room migration would be needed.

### Implementation File: New `ScoringEngine.kt`

Replace `ScoreUtils.kt` with a new `ScoringEngine.kt` that contains:

- `calculateBaseScore(usageMs, limitMs): Float`
- `getStreakMultiplier(streakDays): Float`
- `calculateOvershootPenalty(usageMs, limitMs): Float`
- `calculateAppScore(usageMs, limitMs, streakDays): Float`
- `calculateGlobalScore(appScores): Float`
- `calculateXpEarned(timeSavedMs, isGlobalUnderLimit, streakDays): Int`
- `calculateGoldEarned(timeSavedMs, isGlobalUnderLimit): Int`
- `calculateGemsEarned(streakDays, globalScore): Int`
- `levelForXp(totalXp): Int`
- `xpForNextLevel(currentLevel): Int`
- `rankForLevel(level): Pair<String, String>` (name + emoji)

All pure functions. No dependencies. Easily unit-testable.

---

## Part 1: Current State Audit

### ✅ Fully Implemented & Working

| Layer | What exists | Status |
|-------|-------------|--------|
| **Room Database** | `AionDatabase` with 14 entity tables, 7 DAOs, type converters | ✅ Wired and functional |
| **DI (Hilt)** | `DatabaseModule`, `RepositoryModule` — all DAOs and repos injected | ✅ Complete |
| **Usage Tracking** | `UsageStatsHelper` → `UsageWorker` (15-min periodic) → logs `UsageSessionEntity` rows | ✅ Working |
| **Notifications** | `AionNotificationManager` creates channel, shows system notifications; `NotificationDao`/repo stores them | ✅ Working |
| **App Tracking** | Add/remove apps via `AddAppsScreen` → `AppRepository`; settings (limit, tracking toggle) saved | ✅ Working |
| **Home Screen** | Shows score gauge, usage %, daily/weekly improvement, per-app cards with live data | ✅ Working |
| **Plan Screen** | Lists tracked apps, sort by name/limit/date, delete apps, navigate to details | ✅ Working |
| **App Details** | 3-tab layout (Overview with score + streak, Settings with limit picker, History with grouped sessions) | ✅ Working |
| **Settings** | Theme (Light/Dark/System), accent color picker, permission toggles, persisted via `UserPreferenceEntity` | ✅ Working |
| **Profile** | Display name edit (persisted), avatar placeholder, rank/time-saved stats | ✅ Working |
| **Navigation** | `NavigationSuiteScaffold` + `NavHost` with bottom nav (Home, Plan, Notifications, Settings) + detail routes | ✅ Working |
| **Score System** | `ScoreUtils.calculateScore()` — simple `100 * (1 - usage/limit)` clamped to 0–100 | ✅ Working |
| **Theme** | Light + dark color schemes, dynamic color on Android 12+, `AionTheme(themeMode=)` | ✅ Working |

---

### 🟡 Scaffolded but NOT Connected (Dead Code)

These layers have entities, DAOs, repositories, and DI bindings — but **zero business logic or UI** references them.

| Feature | Files that exist | What's missing |
|---------|-----------------|----------------|
| **Gamification (Points)** | `UserPointsEntity`, `PointsTransactionEntity`, `GamificationDao`, `GamificationRepository` | No logic awards/deducts points. No UI displays balance. **→ Online mode only.** |
| **Achievements** | `AchievementEntity`, `UserAchievementEntity` (same DAO/repo) | No achievements seeded. No conditions evaluated. No UI. **→ Online mode only.** |
| **Shop / Arsenal** | `ShopItemEntity`, `UserInventoryEntity`, `ShopDao`, `ShopRepository` | No items seeded. No purchase flow. No screen. **→ Online mode only.** |
| **Social / Squads** | `SquadEntity`, `SquadMemberEntity`, `SocialDao`, `SocialRepository` | No UI. No invite system. Needs backend. **→ Deferred (future online feature).** |

---

### 🔴 Bugs & Issues Found

#### 1. Duplicate usage sessions on every worker run
**File:** `UsageWorker.kt` (lines 42–53)

The worker runs every 15 minutes and calls `usageRepository.logUsageSession()` with `startTime = todayStart`. This **inserts a new row** every run. Over 24 hours that's ~96 duplicate rows per app per day. Downstream `SUM(totalDurationMs)` aggregations wildly over-count.

**Fix:** Call `resetTodayUsage()` before inserting the new snapshot.

#### 2. Notification detail uses hardcoded sample data
**File:** `MainActivity.kt` (line 166)

```kotlin
NotificationDetailScreen(
    notification = sampleNotifications.first(), // Placeholder
```

Route `id` argument is available but ignored. Detail screen shows `NotificationSpec` sample data instead of actual `NotificationEntity`.

**Fix:** Create `NotificationDetailViewModel` that loads by ID, or query from repo.

#### 3. Accent color saved but never applied to theme
**File:** `Theme.kt`

`AionTheme` accepts `themeMode` but not accent color. User selects accent → it's persisted → theme ignores it.

**Fix:** Add `accentColor` param, map to `Color`, override `colorScheme.primary`.

#### 4. App details header has hardcoded values
**File:** `AppDetailsScreen.kt` (lines 71–75)

`lastOpened = "13:08"`, `dataUsage = "34 MB"`, `notoriety = "HARD"` — all static.

**Fix:** Compute from actual data or remove inapplicable fields.

#### 5. Profile uses TikTok drawable as avatar placeholder
**File:** `ProfileScreen.kt` (line 62)

`avatarRes = R.drawable.tiktok` — the `avatarUri` field on the entity is never used.

#### 6. `getTodayStartMs()` duplicated in 3 ViewModels
**Files:** `HomeViewModel.kt`, `PlanViewModel.kt`, `AppDetailsViewModel.kt`

Should be in `TimeUtils`.

#### 7. Settings FAQ/Feedback/Privacy/Version stubs
**File:** `MainActivity.kt` (lines 135–138) — all `{ /* TODO */ }`.

#### 8. PlanScreen "Remove Apps" menu does nothing
**File:** `PlanScreen.kt` (line 71) — `// Handle remove apps`.

#### 9. Notification sort button is a stub
**File:** `NotificationsScreen.kt` (line 92) — `{ /* Handle sort */ }`.

#### 10. ForeignKey references `packageName` but PK is auto-generated `id`
**File:** `CoreEntities.kt` — works due to unique index but fragile. Low priority.

---

## Part 2: Improvement Plan

### Guiding Principles
1. **Fix bugs before features** — broken fundamentals undermine everything.
2. **Offline mode first** — polish the core experience without gamification complexity.
3. **Online mode layers on top** — gamification, auth, sync are additive; they don't change the core.
4. **Wire existing scaffolding** — the gamification DB plumbing already exists; don't reinvent.
5. **Firebase MCP for backend setup** — use Firebase MCP tools for project config, auth, Firestore rules.

---

## MILESTONE A: Offline Mode (Polish & Ship)

### Phase 1: Fix Critical Bugs

> **CAUTION:** Phase 1 must be completed before any other work. The duplicate sessions bug corrupts all usage data.

#### 1.1 Fix duplicate usage sessions in `UsageWorker`

**File:** `UsageWorker.kt`  
**Change:** Before inserting, delete the existing session for this app + today:

```kotlin
usageRepository.resetTodayUsage(app.packageName, startTime)
```

Uses the already-existing `resetTodayUsage()` method. Each worker run replaces (not appends) the day's snapshot.

**Justification:** All scores, improvements, and history data are wildly inflated without this fix.

#### 1.2 Fix Notification Detail route

**Files:** `MainActivity.kt`, new `NotificationDetailViewModel.kt`  
**Change:**
- Add `getNotificationById(id: Long): Flow<NotificationEntity?>` to `NotificationDao` and `NotificationRepository`.
- Create `NotificationDetailViewModel` that takes `savedStateHandle["id"]` and loads from DB.
- Rewrite `NotificationDetailScreen` to accept `NotificationEntity` (not `NotificationSpec`).
- Wire in `MainActivity.kt`.

**Justification:** Users see wrong data when clicking a notification.

#### 1.3 Apply accent color to theme

**Files:** `Theme.kt`, `MainActivity.kt`  
**Change:**
- Add `accentColor: String = "Purple"` param to `AionTheme`.
- Map string → `Color` (same palette as `AccentSettingsScreen`).
- Override `colorScheme.copy(primary = mappedColor)`.
- Pass `settingsUiState.accentColor` from `MainActivity`.

**Justification:** Feature is presented to the user but does nothing.

#### 1.4 Extract `getTodayStartMs()` to `TimeUtils`

**File:** `TimeUtils.kt`  
**Change:** Add shared function, replace 3 ViewModel copies.

**Justification:** DRY; reduces maintenance.

---

### Phase 2: Onboarding & Mode Selection

This is the **new architectural foundation** for the offline/online split.

#### 2.1 Add `AppMode` to user preferences

**File:** `UserPreferenceEntity` (already key-value), `SettingsViewModel.kt`  
**Change:**
- Store `"app_mode"` preference with values `"offline"` or `"online"`.
- Default: no preference set → show onboarding.
- Add `appMode: String` to `SettingsUiState`.

**Justification:** The mode must be persisted across app restarts.

#### 2.2 Create Onboarding Screen

**Files:** New `OnboardingScreen.kt`, new `OnboardingViewModel.kt`  
**Change:**
- Simple 2–3 screen flow:
  1. **Welcome** — "Welcome to Aion. Take back your time."
  2. **Choose Mode** — two cards:
     - **Offline** — "Track and limit your screen time. No account needed. All data stays on your device."
     - **Online** — "Unlock gamification, achievements, and leaderboards. Requires a Google or email account."
  3. **Permissions** — request Usage Access + Notification permission.
- Save choice to `UserPreferenceEntity("app_mode", "offline"|"online")`.
- Navigate to Home.

**Justification:** Users must choose their mode before using the app. Permissions flow is currently implicit and easy to miss.

#### 2.3 Gate app start on onboarding completion

**File:** `MainActivity.kt`  
**Change:**
- On launch, check if `"app_mode"` preference exists.
- If not → navigate to `OnboardingScreen` as start destination.
- If yes → navigate to `Home` as usual.

**Justification:** First-run experience.

#### 2.4 Add mode toggle in Settings

**File:** `SettingsScreen.kt`, `SettingsViewModel.kt`  
**Change:**
- Add "App Mode" row in Settings → `Offline` / `Online`.
- Changing to Online: show dialog — "This will enable gamification features. You'll need to sign in."
- Changing to Offline: show dialog — "Gamification features will be hidden. Your data is kept locally."
- For now, Online mode shows a "Coming Soon" toast until Phase 6+ is built.

**Justification:** Users should be able to switch anytime.

---

### Phase 3: Offline Mode Polish

#### 3.1 Fix app details header — replace hardcoded values

**File:** `AppDetailsViewModel.kt`, `AppDetailsScreen.kt`  
**Change:**
- `lastOpened`: derive from most recent `UsageSessionEntity.endTime`.
- `dataUsage`: remove entirely (UsageStats API doesn't provide this).
- `notoriety`: compute from usage/limit ratio — `> 80%` → HARD, `50–80%` → MODERATE, `< 50%` → LOW.

**Justification:** Static values feel broken.

#### 3.2 Fix Profile avatar

**File:** `ProfileScreen.kt`  
**Change:**
- If `avatarUri` is non-null, load with Coil.
- If null, show a letter avatar (first letter of display name in a colored circle).
- Remove TikTok drawable usage.

**Justification:** TikTok icon as avatar is confusing.

#### 3.3 Wire Settings stubs

**File:** `MainActivity.kt`, `AionNavigation.kt`  
**Change:**
- Add parameterized route `"settings_detail/{title}"`.
- Route FAQ → static FAQ content, Privacy → placeholder privacy policy, Feedback → email intent, Version → show build info.

**Justification:** Dead click handlers feel broken.

#### 3.4 Remove "Remove Apps" menu item from PlanScreen

**File:** `PlanScreen.kt`  
**Change:** Delete the non-functional menu item. Individual delete already works per-card.

**Justification:** Dead UI erodes trust.

#### 3.5 Add global streak to Home Screen

**File:** `HomeViewModel.kt`, `HomeScreen.kt`  
**Change:**
- Compute consecutive days where total usage < total limit across all tracked apps.
- Display using existing `AionStreakBar` component.
- Works in offline mode — pure local data.

**Justification:** Streaks are a core motivational tool. Currently only visible per-app inside details.

#### 3.6 Mission-themed notification copy

**File:** `UsageWorker.kt`, `AionNotificationManager.kt`  
**Change:** Replace generic messages with themed copy from `ideas.md`:
- Limit: *"⚠️ Intel Alert: {app} has breached the perimeter. Disengage to protect your streak."*
- Half-limit: *"📡 Recon Update: You've used 50% of your {app} allowance. Stay sharp, soldier."*

**Justification:** The 80s-warrior personality is the app's differentiator.

---

### Phase 4: Clean Up Dead Code

#### 4.1 Remove sample data files

**Files:** Delete `NotificationSpec.kt`, `AppDetailSpec.kt`, `ProfileSpec.kt`.

**Justification:** All screens now use real data. `NotificationSpec` actively causes bug #2.

#### 4.2 Remove `Greeting` composable

**File:** `MainActivity.kt` — delete `Greeting()` and its preview.

#### 4.3 Unify util packages

Move `PermissionUtils` from `com.example.aion.utils` to `com.example.aion.util`.

#### 4.4 Hide gamification UI elements in offline mode

**Change:** Ensure no RP, achievements, or shop references appear in offline mode. The existing screens don't currently show them (since the systems aren't wired), but add a mode-check guard for future safety.

---

## MILESTONE B: Online Mode (Firebase + Gamification)

> **Prerequisites:** Milestone A complete. Offline mode is stable and tested.

### Phase 5: Firebase Project Setup

> Uses **Firebase MCP tools** for project configuration.

#### 5.1 Create/configure Firebase project

**Tools:** `firebase_create_project` or `firebase_get_project` (if project exists)  
**Change:**
- Create Firebase project `aion-mobile` (or link existing).
- Create Android app with package `com.example.aion`.
- Download `google-services.json` → place in `app/`.

#### 5.2 Initialize Firebase Auth

**Tools:** `firebase_init`  
**Change:**
- Enable Email/Password auth provider.
- Enable Google Sign-In auth provider.
- Add Firebase Auth SDK to `build.gradle.kts`.

#### 5.3 Initialize Firestore

**Tools:** `firebase_init`  
**Change:**
- Create Firestore database.
- Define security rules: users can only read/write their own document path `/users/{uid}/*`.
- Add Firestore SDK to `build.gradle.kts`.

#### 5.4 Firestore schema design

Collections to mirror gamification Room tables:

```
/users/{uid}/
  profile: { displayName, avatarUri, createdAt }
  points:  { balance }
  
/users/{uid}/transactions/{auto-id}
  { amount, reason, timestamp }

/users/{uid}/achievements/{achievementId}
  { earnedDate }

/users/{uid}/inventory/{auto-id}
  { itemId, purchaseDate }

--- Global (read-only for clients) ---

/achievements/{achievementId}
  { name, condition, pointsReward }

/shop_items/{itemId}
  { name, description, type, cost, content }

--- Leaderboard ---

/leaderboard/{uid}
  { displayName, score, rank, streak, updatedAt }
```

**Justification:** Flat structure mirrors Room tables. User-scoped paths enforce security. Global collections for shared data (achievements catalog, shop catalog) are read-only.

---

### Phase 6: Auth & Account System

#### 6.1 Create Auth screens

**Files:** New `LoginScreen.kt`, new `SignUpScreen.kt`, new `AuthViewModel.kt`  
**Change:**
- `LoginScreen`: email/password fields + Google Sign-In button.
- `SignUpScreen`: email/password + confirm password.
- `AuthViewModel`: wraps Firebase Auth, exposes `authState: StateFlow<AuthState>` (SignedOut, Loading, SignedIn).

#### 6.2 Wire auth into mode switching

**File:** `SettingsViewModel.kt`, `OnboardingScreen.kt`  
**Change:**
- Selecting "Online" during onboarding → navigate to Login/SignUp.
- Switching to "Online" from Settings → navigate to Login/SignUp.
- On successful auth → save `app_mode = "online"` + `firebase_uid` to preferences.
- Switching to "Offline" → sign out of Firebase, save `app_mode = "offline"`.

#### 6.3 Update Profile for online mode

**File:** `ProfileScreen.kt`, `ProfileViewModel.kt`  
**Change:**
- Online: show Firebase user email, photo URL (from Google), sign-out button.
- Offline: show generic local profile (as it is now).

---

### Phase 7: Wire Gamification Points Engine (Online Only)

#### 7.1 Award points in `UsageWorker`

**File:** `UsageWorker.kt`  
**Change:** Only runs gamification logic if `app_mode == "online"`:

```kotlin
if (isOnlineMode) {
    if (totalUsage < totalLimit && totalLimit > 0) {
        val pointsToAward = 10
        gamificationRepository.addTransaction(
            PointsTransactionEntity(amount = pointsToAward, reason = "UNDER_LIMIT_CHECKIN")
        )
        val current = gamificationRepository.getUserPoints().first()
        gamificationRepository.updatePoints(
            (current ?: UserPointsEntity()).copy(balance = (current?.balance ?: 0) + pointsToAward)
        )
        // Sync to Firestore
        firestoreSyncManager.syncPoints(current.balance + pointsToAward)
    }
}
```

**Justification:** Core reward loop — earn RP for staying under limits.

#### 7.2 Display RP on Home and Profile (online mode only)

**Files:** `HomeViewModel.kt`, `HomeScreen.kt`, `ProfileViewModel.kt`, `ProfileScreen.kt`  
**Change:**
- Check mode; if online, combine `gamificationRepository.getUserPoints()` into state.
- Show "⚡ 340 RP" stat card on Home. Show RP balance on Profile.
- If offline, these cards are hidden.

---

### Phase 8: Achievements System (Online Only)

#### 8.1 Seed achievements via `DatabaseCallback`

**File:** New `AionDatabaseCallback.kt`, modify `DatabaseModule.kt`  
**Change:** On `onCreate`, populate `achievements` table:

| ID | Name | Condition | Points |
|----|------|-----------|--------|
| `first_day_under` | First Strike | Stay under limit for 1 day | 50 |
| `three_day_streak` | Triple Threat | 3-day streak under limit | 100 |
| `week_warrior` | Week Warrior | 7-day streak | 250 |
| `month_legend` | Monthly Legend | 30-day streak | 1000 |
| `add_first_app` | Target Acquired | Track your first app | 25 |
| `add_five_apps` | Full Arsenal | Track 5 apps | 75 |
| `score_above_90` | Elite Status | Daily score above 90 | 150 |
| `save_one_hour` | Hour Reclaimed | Save 1 cumulative hour | 100 |

Also push these to the Firestore `/achievements` collection using Firebase MCP.

#### 8.2 Check conditions in `UsageWorker`

**File:** `UsageWorker.kt` or new `AchievementChecker.kt`  
**Change:** Only in online mode. Check streak length, tracked app count, score, time saved. Unlock achievements, award RP, fire themed notifications.

#### 8.3 Display achievements on Profile

**File:** `ProfileScreen.kt`, `ProfileViewModel.kt`  
**Change:** Show achievements grid — earned highlighted, locked grayed. Online mode only.

---

### Phase 9: Shop / Arsenal (Online Only)

#### 9.1 Seed shop items

**File:** `AionDatabaseCallback.kt`  
**Change:** Seed `shop_items` with motivational quotes (50–100 RP) and themes (200–500 RP).

#### 9.2 Build Shop Screen

**Files:** New `ShopScreen.kt`, new `ShopViewModel.kt`  
**Change:**
- Tabs: Quotes, Themes.
- Item cards with name, description, cost, "Buy" button.
- Purchase: deduct RP, add to inventory, log transaction.
- Navigate from Profile or Home.

---

### Phase 10: Cloud Sync & Leaderboard (Online Only)

#### 10.1 Create `FirestoreSyncManager`

**File:** New `FirestoreSyncManager.kt`  
**Change:**
- On each point/achievement/purchase write → mirror to Firestore under `/users/{uid}/`.
- On login → pull Firestore data → merge into Room.
- Conflict resolution: latest timestamp wins.

#### 10.2 Leaderboard

**File:** New `LeaderboardScreen.kt`, Firestore `/leaderboard` collection  
**Change:**
- Worker periodically pushes user's score + streak to `/leaderboard/{uid}`.
- `LeaderboardScreen` reads top-N from Firestore, shows ranked list.
- Accessible from Profile (online mode only).

---

## Summary: Prioritized Phase Order

### Milestone A — Offline Mode (ship first)

| Phase | What | Effort | Impact | Priority |
|-------|------|--------|--------|----------|
| **1. Fix Critical Bugs** | Duplicate sessions, notification detail, accent color, DRY utils | Small | 🔴 Critical | Immediate |
| **2. Onboarding & Mode Selection** | Welcome flow, mode choice, permissions | Medium | 🔴 Critical | Next |
| **3. Offline Polish** | Fix headers, avatar, stubs, streak, themed notifications | Medium | 🟠 High | After 1+2 |
| **4. Clean Up** | Delete dead code, unify packages, guard gamification | Small | 🟢 Low | Last |

### Milestone B — Online Mode (layer on top)

| Phase | What | Effort | Impact | Priority |
|-------|------|--------|--------|----------|
| **5. Firebase Setup** | Project, Auth, Firestore, schema | Medium | 🔴 Foundation | First |
| **6. Auth & Accounts** | Login, signup, mode switching | Medium | 🔴 Foundation | After 5 |
| **7. Points Engine** | Award RP, display balance | Medium | 🟠 High | After 6 |
| **8. Achievements** | Seed, check, display | Medium | 🟠 High | After 7 |
| **9. Shop** | Seed items, purchase flow, screen | Medium | 🟡 Medium | After 8 |
| **10. Sync & Leaderboard** | Firestore sync, leaderboard screen | Large | 🟡 Medium | Last |

---

> **What we are NOT doing (and why):**
> - **Social/Squads** — requires real-time multiplayer backend beyond simple Firestore. Defer to a future version.
> - **Focus Soundtracks** — requires audio assets and media player. Out of scope.
> - **App Blocking (overlay service)** — `SYSTEM_ALERT_WINDOW` is declared but no overlay exists. Major feature; needs its own plan.
> - **In-app purchases** — shop uses earned RP, not real money. No Play Billing needed.

---

**Please review this updated plan and confirm you're happy with the structure. I recommend we start with Phase 1 (bugs) immediately.**
