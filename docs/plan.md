# Aion App Overview

Aion is a focus and usage-control app for tracking time spent in apps, reviewing usage history, setting daily limits, and guiding the user toward healthier phone habits. The current UI centers on four bottom-nav areas: Home, Plan, Notifications, and Settings, with drill-down screens for app details, notification details, profile editing, and settings sub-pages.

## Current Features
- Home dashboard with a greeting header, summary gauges, stat cards, and tappable app cards.
- Plan screen with app cards, add-app flow, and app detail drill-down.
- Notifications screen with read and unread tabs, a mark-all-as-read action, and notification detail drill-down.
- Settings screen with theme, accent, FAQ, feedback, privacy, version, and profile access.
- Profile screen for editing the username and profile picture.
- Shared detail screens for app details, notification details, and settings sub-pages.

## Current Screens

### Home Screen
- Purpose: Main dashboard showing the user’s summary and tracked apps.
- Components: `AionTopAppBar`, `AionHomeHeader`, `AionProgressGauge`, `AionStatCard`, `PlanAppCard`.

### Plan Screen
- Purpose: Lists tracked apps and supports moving into the add-app flow.
- Components: `AionTopAppBar`, `SortHeader`, `PlanAppCard`, overflow menu actions, `AddAppsScreen` entry.

### Add Apps Screen
- Purpose: Lets the user browse and search available apps to add to the plan.
- Components: `AionTopAppBar`, `AionSearchBar`, `SortHeader`, `AddAppCard`.

### Notifications Screen
- Purpose: Shows notifications split into unread and read tabs, with a menu action to mark everything as read.
- Components: `AionTopAppBar`, overflow ellipsis menu, `NotificationTabs`, `SortHeader`, `NotificationItem`.

### Notification Detail Screen
- Purpose: Shows the full content of a selected notification.
- Components: `AionTopAppBar`, notification image preview, title, timestamp, message text.

### App Details Screen
- Purpose: Displays detailed usage information for a selected app and lets the user inspect overview, settings, and history.
- Components: `AionTopAppBar`, `AppDetailsHeader`, `AionTabs`, `AionProgressGauge`, `AionStreakBar`, `AionLimitPicker`, `AionToggleCard`, `AionHistoryItem`, `AionFilledButton`.

### Settings Screen
- Purpose: Central preferences screen for app behavior and navigation into deeper settings.
- Components: `AionTopAppBar`, `AionProfileActionButton`, `AionSettingsSection`, `AionSettingsRow`, `AionThemeOption`, `AionAccentColorOption`.

### Theme Settings Screen
- Purpose: Lets the user choose between light and dark mode.
- Components: `AionTopAppBar`, `AionThemeOption`.

### Accent Settings Screen
- Purpose: Lets the user pick the accent color used throughout the app.
- Components: `AionTopAppBar`, `AionAccentColorOption`.

### Settings Detail Screens
- Purpose: Shared detail pages for settings-related information pages.
- FAQ: Answers common questions using `SettingsDetailScreen` and `AionTopAppBar`.
- Feedback: Lets the user read the feedback page using `SettingsDetailScreen` and `AionTopAppBar`.
- Privacy Policy: Shows privacy information using `SettingsDetailScreen` and `AionTopAppBar`.
- Version: Displays the app version using `SettingsDetailScreen` and `AionTopAppBar`.

### Profile Screen
- Purpose: Allows the user to update their username and profile picture and inspect profile info.
- Components: `AionTopAppBar`, `ProfileHeaderCard`, `ProfilePictureCard`, `ProfileUsernameCard`, `AionSettingsSection`, `AionSettingsRow`.

## Aion Architecture Roadmap

To transform the current static UI into a fully functional application, I recommend a **Clean Architecture** approach using **Room** for persistence and **Hilt** for dependency injection.

## Phase 1: Data Layer (The Foundation)
1.  **Room Persistence**:
    - **Flexible Schema**: Implement the schema described in `ideas.md` to support both current tracking and future features (Gamification, Shop, Achievements).
    - **Core Entities**:
        - `TrackedAppEntity`: Package name, app name, icon resource/URI.
        - `AppSettingsEntity`: Daily limits, tracking toggles, notification preferences.
        - `UsageSessionEntity`: Start/end timestamps, duration, package name.
        - `NotificationEntity`: Title, message, image, timestamp, type.
        - `UserProfileEntity`: Username, avatar, created date.
        - `UserPreferenceEntity`: Theme mode, accent color (Key-Value store).
    - **Future-Proof Entities**:
        - `UserPointsEntity` & `PointsTransactionEntity` (Gamification).
        - `ShopItemEntity` & `UserInventoryEntity` (Arsenal/Shop).
        - `AchievementEntity` & `UserAchievementEntity` (Rewards).
        - `SquadEntity` & `SquadMemberEntity` (Social).
    - **Database**: Implement the Room database singleton with standard DAOs.
2.  **Repositories**:
    - Create `AppRepository` and `UsageRepository` to act as the single source of truth for the ViewModels.

## Phase 2: Logic & Integration (The Brain)
1.  **UsageStatsManager Integration**:
    - Replace mock usage data with real system data from Android's usage stats.
2.  **WorkManager**:
    - Implement a background worker to periodically sync app usage and trigger notifications if limits are exceeded.
3.  **ViewModels**:
    - Migrate screen state from `remember { }` to `ViewModel` with `StateFlow`.
    - Move filtering and sorting logic from the UI into ViewModels.

## Phase 3: Navigation & DI Refactor (The Skeleton)
1.  **Dependency Injection (Hilt)**:
    - Set up Hilt to provide database and repository instances to ViewModels automatically.
2.  **Navigation Component**:
    - Refactor `MainActivity` navigation to use the Jetpack Compose Navigation library (Safe Args support).

## Phase 4: Premium Design & Polish (The Skin)
1.  **Custom Glassmorphism**:
    - Build a `GlassCard` modifier using `Modifier.blur()` and `graphicsLayer`.
2.  **Animations**:
    - Implement smooth screen transitions.
    - Add Lottie animations for empty states or success feedback.
3.  **M3 Customization**:
    - Replace remaining Material 3 defaults with brand-specific custom components.

---

### Next Immediate Steps:
1.  Add Room and Hilt dependencies to `build.gradle`.
2.  Define all database entities (including future-proof ones) in the `data` package.
3.  Create the Room Database class and basic DAOs.
