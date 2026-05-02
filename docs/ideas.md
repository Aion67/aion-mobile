---

## 1. Targeted features that make the app unique … and truly help people scroll less

The 80s‑warrior / resistance theme is a great emotional hook, but it must serve the **core mission**: reducing mindless screen time. Here’s how to bake that personality into features that directly drive healthier behaviour.

### A. Focus Missions (Challenges you can’t ignore)
Instead of a generic “daily limit”, turn each day into a retro‑themed mission:

- **Operation: Deep Focus** – pick 1–3 apps to stay *under* a strict time budget. Completing the mission earns **Resistance Points**.
- **Sabotage the Feed** – block an app entirely for a set number of hours. Success builds a streak, failure resets it.
- **Recon Reports** – the app visually compares today’s usage against your average, framed as “enemy activity has dropped 40%!”

👉 *Why it helps*: Missions give immediate, concrete goals that replace the vague “use less” intention.

### B. The Arsenal (Shop that rewards good habits)
Use Resistance Points to unlock content that **reinforces focus and motivation**, not shiny gimmicks:

- **Motivational Quotes & Stories** – real accounts of people who regained control over their digital lives. Unlockable as “Field Manuals”.
- **Focus Soundtracks / White Noise** – 80s synth loops or nature sounds. Buy with points, listen while working.
- **Theme Armour** – cosmetic app themes (cyberpunk, retro‑terminal) that celebrate your progress.

👉 *Why it helps*: The shop becomes a tool to stay inspired, not a distraction. Earning an inspiring quote after a successful day reinforces the habit loop.

### C. Squad & Leaderboards (Optional, private social pressure)
- **Resistance Cells** – invite 2‑3 friends. See only each other’s *weekly screen‑time reduction %* (not raw hours). The winner gets a badge visible only inside the cell.
- **Global Raids** – community goals, e.g., “As a collective, reclaim 1M minutes from social media this month.”

👉 *Why it helps*: Light social accountability, without turning into another addictive feed.

### D. Visual & Narrative Identity
- **Dashboard as Command Center** – use wireframe/terminal UI elements, but keep them highly legible.
- **Status Bar** – “System Uptime: 3 days since last relapse” instead of “Streak: 3”.
- **Push notifications** written as mission briefings: “Intel shows Instagram is planning an ambush. Stay off it for 2 hours to earn +50 RP.”

All of this stays tightly aligned with **spending less time scrolling**, because every reward is earned by behaviour change, not by engagement with the app itself.

---

## 2. A flexible Room schema that supports current features and future expansion

You need a schema that won’t break when you later add gamification, purchases, or social features. The plan below separates **core tracking** from **extendable user‑facing modules** and uses Room’s migration system to add tables safely.

### Core Entities (today’s functionality)

```kotlin
// ────────── 1. The user profile  ──────────
@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Long = 1,           // single user for now
    val username: String,
    val avatarUri: String?,
    val createdAt: Long
)

// ────────── 2. Installed / tracked apps  ──────────
@Entity(tableName = "tracked_apps")
data class TrackedAppEntity(
    @PrimaryKey val packageName: String,
    val appName: String,
    val iconResName: String?,
    val isTrackingEnabled: Boolean = true,
    val sortOrder: Int = 0
)

// ────────── 3. App‑specific settings  ──────────
@Entity(
    tableName = "app_settings",
    foreignKeys = [ForeignKey(
        entity = TrackedAppEntity::class,
        parentColumns = ["packageName"],
        childColumns = ["packageName"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class AppSettingsEntity(
    @PrimaryKey val packageName: String,
    val dailyLimitMinutes: Int = 60,
    val limitNotificationEnabled: Boolean = true,
    val blockInsteadOfWarn: Boolean = false
)

// ────────── 4. Usage records (one row per session)  ──────────
@Entity(tableName = "usage_sessions")
data class UsageSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(index = true) val packageName: String,
    val startTime: Long,
    val endTime: Long?,
    val totalForegroundMs: Long = 0
)

// ────────── 5. Notifications  ──────────
@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val message: String,
    val imageUri: String?,
    val timestamp: Long,
    val isRead: Boolean = false,
    val type: String = "limit_exceeded"    // "mission", "achievement", etc.
)

// ────────── 6. User preferences (theme, accent)  ──────────
@Entity(tableName = "user_preferences")
data class UserPreferenceEntity(
    @PrimaryKey val key: String,           // e.g., "theme_mode", "accent_color"
    val value: String
)
```

**Why this works today:**
- All screens you listed (Home, Plan, Notifications, Settings, Profile, App Details) can be populated with these tables.
- `usage_sessions` can be aggregated in DAO queries for daily totals, app details history, and gauge values.
- `user_preferences` acts as a flexible key‑value store for settings that might change (e.g., add “enable_squad” later without altering schema).

---

### Future‑proof expansion tables (add with Room migrations)

When you’re ready for gamification, points, shop, and achievements, you’ll add these tables – *without touching the core ones*.

```kotlin
// ────────── 7. Gamification: Points & Currency  ──────────
@Entity(tableName = "user_points")
data class UserPointsEntity(
    @PrimaryKey val userId: Long = 1,
    val balance: Int = 0
)

@Entity(tableName = "points_transactions")
data class PointsTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val amount: Int,
    val reason: String,             // "daily_mission_complete", "purchase"
    val timestamp: Long
)

// ────────── 8. Shop / Arsenal  ──────────
@Entity(tableName = "shop_items")
data class ShopItemEntity(
    @PrimaryKey val itemId: String,     // "quote_collection_1", "theme_cyber", "sound_rain"
    val name: String,
    val description: String,
    val type: String,                   // "quote", "theme", "sound"
    val cost: Int,
    val contentJson: String            // actual quote text, theme color codes, audio URI
)

@Entity(tableName = "user_inventory")
data class UserInventoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val itemId: String,
    val purchasedAt: Long,
    val isEquipped: Boolean = false
)

// ────────── 9. Achievements  ──────────
@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val achievementId: String,  // "first_week_under_limit"
    val name: String,
    val description: String,
    val iconRes: String?,
    val pointsReward: Int
)

@Entity(tableName = "user_achievements")
data class UserAchievementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val achievementId: String,
    val unlockedAt: Long
)

// ────────── 10. (Optional) Squad / Social  ──────────
@Entity(tableName = "squads")
data class SquadEntity(
    @PrimaryKey val squadId: String,
    val name: String,
    val inviteCode: String,
    val createdAt: Long
)

@Entity(tableName = "squad_members")
data class SquadMemberEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val squadId: String,
    val userId: Long,
    val weeklyScreenReductionPct: Float = 0f
)
```

### Why this schema is extensible
1. **Separation of concerns** – core usage tracking lives in its own tables. New modules (shop, achievements, social) are isolated.
2. **No schema changes needed on existing tables** – all new features reference `userId` and newly created tables.
3. **Room migrations are straightforward** – from version 1 to 2 you just `CREATE` the new tables. No `ALTER` that could fail.
4. **`contentJson` in shop items** lets you store arbitrary content (quotes, theme config) without creating dozens of columns.
5. **`user_preferences` key‑value store** can handle any future toggle or setting without a schema update.

### Next steps to integrate
- Define DAOs that expose `Flow<List<...>>` for reactive UI.
- In the repository, add methods to reward points when a daily limit is respected (based on `usage_sessions` aggregation).
- When the shop is unlocked, query `user_points` and `user_inventory` to display available items.

---

This approach solves both of your concerns: you get a unique, sticky experience that **directly combats the scrolling habit**, and a data layer that’s sturdy enough to grow with every wild idea you later decide to ship.