# Aion Implementation Progress

## Project Status: UI Refinement & Feature Expansion (Phase 5)

This document tracks the progress of transforming the Aion static UI into a functional, data-driven application following Clean Architecture principles.

---

### Phase 1: Data Layer (The Foundation) - COMPLETED
- [x] Add Room and Hilt dependencies to `build.gradle`
- [x] Define core database entities (`TrackedApp`, `AppSettings`, `UsageSession`, `Notification`)
- [x] Define user profile and preference entities
- [x] Define future-proof entities (Gamification, Shop, Achievements, Social)
- [x] Create Room DAOs for core functionality
- [x] Implement Room Database singleton
- [x] Set up Hilt for Dependency Injection
- [x] Implement Repository layer (App, Usage, Notification)
- [x] Implement remaining repositories (User, Gamification, Shop - Future proof)
- [x] Implementation of Type Converters (e.g., for Dates or complex types)

### Phase 2: Logic & Integration (The Brain) - COMPLETED
- [x] UsageStatsManager Integration
- [x] WorkManager for background tracking
- [x] Notification triggering logic

### Phase 3: Navigation & DI Refactor (The Skeleton) - COMPLETED
- [x] ViewModel migration (StateFlow)
- [x] Jetpack Navigation component integration
- [x] Hilt injection for ViewModels

### Phase 4: Core Functionality & Data Integrity (The Gaps) - COMPLETED
**Home Screen**
- [x] Implement dynamic "Score" calculation (Gamification logic)
- [x] Implement live "Improvement" statistics (Yesterday vs Today comparisons)
- [x] Fetch actual App Icons from package names instead of using placeholders

**Plan Screen**
- [x] Implement App Removal from tracking list
- [x] Implement dynamic Used Time fetching in Plan cards
- [x] Connect "Sort" functionality to actual data ordering

**Add Apps Screen**
- [x] Implement system-installed apps fetching (PackageManager)
- [x] Implement Multi-select to add multiple apps at once (via individual toggles)
- [x] Implement "Add" functionality (Saving to `TrackedAppEntity` and `AppSettingsEntity`)
- [x] Distinguish between already tracked and untracked apps in the list

**Profile Screen**
- [ ] Implement Profile Picture change (Gallery/Camera integration)
- [x] Implement Username editing and persistence
- [ ] Display real stats (Total time saved, rank, etc.)

**Settings Screen**
- [x] Persist Theme selection (Light/Dark/System)
- [x] Persist Accent Color selection across the app
- [x] Implement Notification toggle (Updating `AppSettingsEntity` globally or per app)
- [x] Implement "Display over other apps" permission request logic
- [x] Implement "Usage access" permission request logic

### Phase 5: New Features & Bug Fixes (The Polish) - IN PROGRESS
**UI Components Library (New/Refactored)**
- [x] `AionTopAppBar`: Centered title, leading/trailing icons
- [x] `SortHeader`: "Apps / Sort by" header
- [x] `AppDetailsHeader`: Large icon + metadata table
- [x] `AionTabs`: Overview/Settings/History tabs
- [x] `AionStreakBar`: Enhanced 7-day streak ring view
- [x] `AionActionButtons`: Filled, Outlined, and Icon buttons
- [x] `AionSearchBar`: Rounded search field with clear action
- [x] `AionLimitPicker`: HH:MM:SS duration selector
- [x] `AionToggleCard`: Settings card with label/description/switch
- [x] `AionHistoryItem`: Non-interactable history list item

**Onboarding**
- [ ] Create Onboarding flow (Welcome, Permission requests, Initial app selection)
- [ ] Implement first-launch detection logic

**Theme Consistency & UI Bugs**
- [ ] **Fix Dark/Light mode inconsistency**: Migrate components from hardcoded `Variables.kt` to `MaterialTheme.colorScheme`
- [ ] **Fix "White on White" text issues**: Specifically in Light mode where text might be invisible
- [ ] Ensure all screens respect the System/Selected theme globally

**App Details Screen**
- [x] Assemble multi-tab `AppDetailsScreen`
- [x] Bind real data to App Details (Usage charts, stats)
- [x] Implement limit editing and settings persistence
- [x] Implement real History data fetching and display

**Profile & Settings**
- [ ] Implement actual profile editing (Bio, Display Name)
- [ ] Link FAQ, Feedback, and Privacy screens to real content/actions

### Phase 6: Premium Design & Polish (The Skin) - TODO
- [ ] Glassmorphism UI components
- [ ] Animation implementation
- [ ] Lottie integration

---

## Completed Tasks
- [x] Initial UI Screens and Components (Static)
- [x] AddAppsScreen implementation
- [x] TopAppBar refactoring (Profile on left, menu on right)
- [x] Implementation of Plan and Notification screens
- [x] Architecture Roadmap definition
- [x] Dynamic Score and Improvement statistics
- [x] Real-time usage data in Plan screen
- [x] Sorting and App removal logic
- [x] Componentized UI library based on Figma
- [x] Fully functional App Details Screen (Settings & History)
- [x] Permission management (Usage, Overlay)
