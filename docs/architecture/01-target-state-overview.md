# Target State Overview

## Goals
- Move from UI-first prototype to layered architecture with clear state ownership.
- Keep Compose UI declarative and stateless where possible.
- Support onboarding, auth, planner limits, insights analytics, and settings persistence.

## Architecture Layers
```mermaid
flowchart TD
    A[Android App / MainActivity] --> B[Navigation Host + Session Gate]
    B --> C[Presentation Layer]
    C --> C1[Composable Screens]
    C --> C2[ViewModels]
    C2 --> D[Domain Layer]
    D --> D1[Use Cases]
    D --> D2[Domain Models]
    D --> E[Data Layer]
    E --> E1[Repositories]
    E1 --> E2[Room Database]
    E1 --> E3[DataStore]
    E1 --> E4[Network APIs]
```

## Module Boundaries (Single-module today, modular-ready contracts)
```mermaid
classDiagram
    class MainActivity
    class EnafAppNavHost
    class SessionCoordinator

    class OnboardingViewModel
    class AuthViewModel
    class HomeViewModel
    class PlannerViewModel
    class InsightsViewModel
    class SettingsViewModel

    class OnboardingRepository
    class AuthRepository
    class UsageRepository
    class SettingsRepository
    class InsightsRepository

    MainActivity --> EnafAppNavHost
    EnafAppNavHost --> SessionCoordinator
    EnafAppNavHost --> OnboardingViewModel
    EnafAppNavHost --> AuthViewModel
    EnafAppNavHost --> HomeViewModel
    EnafAppNavHost --> PlannerViewModel
    EnafAppNavHost --> InsightsViewModel
    EnafAppNavHost --> SettingsViewModel

    PlannerViewModel --> UsageRepository
    HomeViewModel --> UsageRepository
    InsightsViewModel --> InsightsRepository
    SettingsViewModel --> SettingsRepository
    AuthViewModel --> AuthRepository
    OnboardingViewModel --> OnboardingRepository
```

## Route Map (Target)
- Pre-auth: `onboarding`, `auth/login`, `auth/signup`.
- Post-auth shell: `home`, `planner`, `insights`, `settings`.
- Overlay/global: `times_up_modal`, `profile`, `notifications`.

## Delivery Phases
1. Add NavHost and session gate (onboarding/auth/main shell).
2. Introduce ViewModel per screen with `UiState` and `UiEvent`.
3. Add repositories + local persistence (Room/DataStore).
4. Connect analytics/insights and app usage data provider.
5. Add integration/UI tests for critical flows.

