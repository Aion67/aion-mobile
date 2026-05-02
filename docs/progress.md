# Aion Implementation Progress

## Project Status: Transitioning to Data Layer (Phase 1)

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

### Phase 4: Core Functionality & Data Integrity (The Gaps) - IN PROGRESS
**Home Screen**
- [ ] Implement dynamic "Score" calculation (Gamification logic)
- [ ] Implement live "Improvement" statistics (Yesterday vs Today comparisons)
- [ ] Fetch actual App Icons from package names instead of using placeholders

**Plan Screen**
- [x] Implement App Removal from tracking list
- [ ] Implement dynamic Used Time fetching in Plan cards
- [ ] Connect "Sort" functionality to actual data ordering

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
- [ ] Implement "Display over other apps" permission request logic

### Phase 5: Premium Design & Polish (The Skin) - TODO
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
