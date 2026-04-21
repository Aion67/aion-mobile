# Enaf System Architecture (Unified Target State)

This document is the single high-level blueprint for the full application, from app launch to authenticated usage flows.

## 1) System Scope
- Platform: Android, Jetpack Compose.
- App phases: onboarding, authentication, main productivity shell.
- Main tabs: home, planner, insights, settings.
- Architecture style: layered MVVM + repository + local-first storage.

## 2) Layered Architecture
```mermaid
flowchart TD
    A[MainActivity] --> B[EnafAppRoot/NavHost]
    B --> C[Presentation Layer]
    C --> C1[Composable Screens]
    C --> C2[ViewModels]

    C2 --> D[Domain Layer]
    D --> D1[Use Cases]
    D --> D2[Domain Models]

    D1 --> E[Data Layer]
    E --> E1[Repositories]
    E1 --> E2[Room DB]
    E1 --> E3[DataStore]
    E1 --> E4[Remote APIs]
    E1 --> E5[System Usage Stats API]
    E1 --> E6[Secure Token Store]
```

## 3) Core Class Diagram
```mermaid
classDiagram
    class MainActivity {
      +onCreate()
    }

    class EnafAppRoot {
      +start()
      +renderNavHost()
    }

    class SessionCoordinator {
      +loadSessionState()
      +markOnboardingSeen()
      +persistSession(token,user)
      +clearSession()
    }

    class OnboardingViewModel {
      +state: StateFlow~OnboardingUiState~
      +effects: SharedFlow~OnboardingEffect~
      +onEvent(event)
    }

    class AuthViewModel {
      +state: StateFlow~AuthUiState~
      +effects: SharedFlow~AuthEffect~
      +onEvent(event)
    }

    class HomeViewModel {
      +state: StateFlow~HomeUiState~
      +refresh()
    }

    class PlannerViewModel {
      +state: StateFlow~PlannerUiState~
      +onEvent(event)
    }

    class InsightsViewModel {
      +state: StateFlow~InsightsUiState~
      +onPeriodChanged(period)
    }

    class SettingsViewModel {
      +state: StateFlow~SettingsUiState~
      +onToggleChanged(key,value)
      +signOut()
    }

    class OnboardingRepository
    class AuthRepository
    class UsageRepository
    class InsightsRepository
    class SettingsRepository

    class AppDatabase
    class PreferencesStore
    class TokenStore
    class RemoteApi
    class UsageStatsProvider

    MainActivity --> EnafAppRoot
    EnafAppRoot --> SessionCoordinator

    EnafAppRoot --> OnboardingViewModel
    EnafAppRoot --> AuthViewModel
    EnafAppRoot --> HomeViewModel
    EnafAppRoot --> PlannerViewModel
    EnafAppRoot --> InsightsViewModel
    EnafAppRoot --> SettingsViewModel

    OnboardingViewModel --> OnboardingRepository
    AuthViewModel --> AuthRepository
    HomeViewModel --> UsageRepository
    PlannerViewModel --> UsageRepository
    InsightsViewModel --> InsightsRepository
    SettingsViewModel --> SettingsRepository
    SettingsViewModel --> SessionCoordinator

    OnboardingRepository --> PreferencesStore
    AuthRepository --> RemoteApi
    AuthRepository --> TokenStore
    UsageRepository --> AppDatabase
    UsageRepository --> UsageStatsProvider
    InsightsRepository --> AppDatabase
    InsightsRepository --> RemoteApi
    SettingsRepository --> PreferencesStore
```

## 4) End-to-End Sequence Diagram
```mermaid
sequenceDiagram
    autonumber
    participant U as User
    participant A as MainActivity/EnafAppRoot
    participant S as SessionCoordinator
    participant O as OnboardingVM
    participant AU as AuthVM
    participant AR as AuthRepository
    participant H as HomeVM
    participant P as PlannerVM
    participant I as InsightsVM
    participant ST as SettingsVM

    U->>A: Launch app
    A->>S: loadSessionState()
    alt onboarding not completed
        A->>O: init onboarding
        U->>O: Skip/Get Started
        O->>S: markOnboardingSeen()
    end

    alt not authenticated
        A->>AU: show auth
        U->>AU: submit credentials
        AU->>AR: signIn/signUp
        AR-->>AU: success(token,user)
        AU->>S: persistSession(token,user)
    end

    A->>H: load Home tab
    H-->>U: dashboard data

    U->>P: change app limit/search
    P-->>U: updated planner state

    U->>I: switch period weekly/monthly/yearly
    I-->>U: refreshed insights

    U->>ST: toggle settings or sign out
    ST->>S: clearSession() (on sign out)
    S-->>A: navigate(auth/login)
```

## 5) Activity Diagram: App Startup and Routing
```mermaid
flowchart TD
    Start([App Start]) --> Load[Load Session State]
    Load --> OnboardCheck{Onboarding Seen?}
    OnboardCheck -- No --> Onboard[Show Onboarding]
    Onboard --> MarkSeen[Save onboarding complete]
    MarkSeen --> AuthCheck

    OnboardCheck -- Yes --> AuthCheck{Authenticated?}
    AuthCheck -- No --> Auth[Show Auth Screen]
    Auth --> AuthResult{Auth Success?}
    AuthResult -- No --> Auth
    AuthResult -- Yes --> Main

    AuthCheck -- Yes --> Main[Enter Main Tab Shell]
    Main --> End([Ready])
```

## 6) Activity Diagram: Planner Limit Update
```mermaid
flowchart TD
    A([User opens Planner]) --> B[Load limits + usage]
    B --> C[Render app list]
    C --> D{User action}
    D -- Search --> E[Filter list]
    E --> C
    D -- Change limit --> F[Validate value]
    F --> G{Valid?}
    G -- No --> H[Show inline error]
    H --> C
    G -- Yes --> I[Persist via UpdateAppLimitUseCase]
    I --> J[Refresh state and show saved feedback]
    J --> C
```

## 7) Activity Diagram: Settings and Sign-out
```mermaid
flowchart TD
    A([Settings Screen]) --> B{Action Type}
    B -- Toggle / slider / theme --> C[Update SettingsViewModel]
    C --> D[Persist to DataStore]
    D --> E[Emit updated UiState]
    E --> A

    B -- Sign out --> F[Clear token/session]
    F --> G[Navigate to Auth Login]
```

## 8) Navigation Graph (Target)
- `onboarding`
- `auth/login`
- `auth/signup`
- `main/home`
- `main/planner`
- `main/insights`
- `main/settings`
- Optional overlays: `times_up_modal`, `profile`, `notifications`

## 9) State Ownership Rules
- Composables render from `UiState` and dispatch `UiEvent` only.
- ViewModels own business-relevant screen state.
- Repositories are the only data source abstraction used by ViewModels.
- Session state (onboarding + auth) is centralized in `SessionCoordinator`.

## 10) Delivery Order
1. Replace direct tab switching in `EnafApp()` with NavHost + route graph.
2. Introduce ViewModels and move `remember` business state into them.
3. Implement repositories for auth, usage, insights, and settings.
4. Add Room/DataStore/token storage and wire domain use cases.
5. Add tests for startup routing, auth, planner updates, and sign-out.
6. Refine onboarding flow with new session management.
7. Enhance error handling and data freshness strategies.
8. Optimize performance for large data sets in planner and insights.
9. Polish UI/UX based on testing feedback.

## 11) System Use Case Map
```mermaid
flowchart LR
    U[User] --> UC1[Complete Onboarding]
    U --> UC2[Create Account / Login]
    U --> UC3[View Daily Summary]
    U --> UC4[Set App Limits]
    U --> UC5[Track Insights]
    U --> UC6[Manage Settings]
    U --> UC7[Sign Out]

    UC1 --> S1[SessionCoordinator]
    UC2 --> S2[AuthViewModel/AuthRepository]
    UC3 --> S3[HomeViewModel/UsageRepository]
    UC4 --> S4[PlannerViewModel/UsageRepository]
    UC5 --> S5[InsightsViewModel/InsightsRepository]
    UC6 --> S6[SettingsViewModel/SettingsRepository]
    UC7 --> S1
```

## 12) Container Diagram (App Runtime)
```mermaid
flowchart TD
    subgraph AndroidApp[Enaf Android App]
        UI[Compose UI]
        VM[ViewModels]
        UC[Use Cases]
        REPO[Repositories]
    end

    subgraph Device[Device Storage/Services]
        ROOM[(Room DB)]
        DS[(DataStore)]
        SS[Usage Stats Service]
        TS[(Secure Token Store)]
    end

    subgraph Cloud[Backend]
        API[Auth/Profile/Insights API]
    end

    UI --> VM --> UC --> REPO
    REPO --> ROOM
    REPO --> DS
    REPO --> SS
    REPO --> TS
    REPO --> API
```

## 13) Sequence Diagram: Planner Save and Re-query
```mermaid
sequenceDiagram
    autonumber
    participant U as User
    participant UI as PlannerScreen
    participant VM as PlannerViewModel
    participant UC as UpdateAppLimitUseCase
    participant R as UsageRepository
    participant DB as Room

    U->>UI: Change limit slider/input
    UI->>VM: OnAppLimitChanged(app, minutes)
    VM->>UC: execute(app, minutes)
    UC->>R: updateLimit(app, minutes)
    R->>DB: upsert AppUsageLimit
    DB-->>R: success
    R-->>UC: success
    UC-->>VM: success
    VM->>R: getPlannerState(search, sort)
    R-->>VM: latest app limits + usage
    VM-->>UI: UiState(updated)
    VM-->>UI: UiEffect.ShowSaved
```

## 14) Sequence Diagram: Insights Refresh by Period
```mermaid
sequenceDiagram
    autonumber
    participant U as User
    participant UI as InsightsScreen
    participant VM as InsightsViewModel
    participant UC as GetInsightsUseCase
    participant R as InsightsRepository
    participant DB as Room
    participant API as Remote API

    U->>UI: Tap Monthly/Yearly tab
    UI->>VM: OnPeriodChanged(period)
    VM->>UC: execute(period)
    UC->>R: getInsights(period)
    R->>DB: read cached aggregates
    alt stale or missing data
        R->>API: fetch aggregates(period)
        API-->>R: latest aggregates
        R->>DB: upsert aggregates
    end
    R-->>UC: InsightsDomainModel
    UC-->>VM: mapped UiModel
    VM-->>UI: UiState(distribution, streak, observations)
```

## 15) Activity Diagram: Error and Retry Pattern
```mermaid
flowchart TD
    A([User action]) --> B[Dispatch UiEvent]
    B --> C[ViewModel validates]
    C --> D{Valid input?}
    D -- No --> E[Emit UiState with field error]
    E --> Z([Wait for input])

    D -- Yes --> F[Execute use case]
    F --> G{Result}
    G -- Success --> H[Emit UiState updated]
    G -- Recoverable error --> I[Emit UiEffect snackbar/retry]
    I --> J{User retries?}
    J -- Yes --> F
    J -- No --> Z
    G -- Non-recoverable --> K[Emit blocking error state]
```

## 16) Activity Diagram: Data Freshness Lifecycle
```mermaid
flowchart TD
    A([Open screen]) --> B[Load local cached data]
    B --> C[Render immediately]
    C --> D[Start background sync]
    D --> E{Remote success?}
    E -- Yes --> F[Merge + persist new data]
    F --> G[Emit refreshed UiState]
    E -- No --> H[Keep cached data + show stale hint]
```

## 17) Implementation Mapping to Current File Structure
- Entry point and shell: `app/src/main/java/com/example/enaf/MainActivity.kt`
- Onboarding flow UI: `app/src/main/java/com/example/enaf/ui/screens/onboarding/OnboardingScreen.kt`
- Auth flow UI: `app/src/main/java/com/example/enaf/ui/screens/auth/AuthScreen.kt`
- Main tabs UI:
  - `app/src/main/java/com/example/enaf/ui/screens/home/HomeScreen.kt`
  - `app/src/main/java/com/example/enaf/ui/screens/planner/PlannerScreen.kt`
  - `app/src/main/java/com/example/enaf/ui/screens/insights/InsightsScreen.kt`
  - `app/src/main/java/com/example/enaf/ui/screens/settings/SettingsScreen.kt`
- Shared UI components: `app/src/main/java/com/example/enaf/ui/components/`
- Theme and design tokens: `app/src/main/java/com/example/enaf/ui/theme/`

## 18) Target Package Layout
```mermaid
flowchart TD
    A[com.example.enaf]
    A --> A1[app.navigation]
    A --> A2[app.session]
    A --> A3[feature.onboarding]
    A --> A4[feature.auth]
    A --> A5[feature.home]
    A --> A6[feature.planner]
    A --> A7[feature.insights]
    A --> A8[feature.settings]
    A --> A9[core.domain]
    A --> A10[core.data]
    A --> A11[core.database]
    A --> A12[core.preferences]
    A --> A13[core.designsystem]
```

## 19) Definition of Done for Architecture Completion
- NavHost routes implemented for onboarding, auth, and main shell.
- All business state moved from `remember` to feature ViewModels.
- Repository contracts implemented with local-first behavior.
- Settings and session persistence wired to DataStore/token store.
- Planner/insights flows backed by real data source contracts.
- Unit, integration, and UI navigation tests passing in CI.

