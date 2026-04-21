# Onboarding and Auth Flow (Target)

## End-to-End Journey
```mermaid
sequenceDiagram
    autonumber
    participant User
    participant App as MainActivity/NavHost
    participant Session as SessionCoordinator
    participant OnboardingVM as OnboardingViewModel
    participant AuthVM as AuthViewModel
    participant AuthRepo as AuthRepository

    User->>App: Launch app
    App->>Session: loadSessionState()
    Session-->>App: requiresOnboarding = true
    App->>OnboardingVM: init()
    OnboardingVM-->>User: Show onboarding pages

    User->>OnboardingVM: Tap Get Started
    OnboardingVM->>Session: markOnboardingSeen()
    Session-->>App: navigate(auth/signup)

    User->>AuthVM: Submit credentials
    AuthVM->>AuthRepo: signUp(email, password)
    AuthRepo-->>AuthVM: success(token, user)
    AuthVM->>Session: persistSession(token, user)
    Session-->>App: navigate(main/home)
```

## Auth Decision Tree
```mermaid
flowchart TD
    A[App Start] --> B{Onboarding completed?}
    B -- No --> C[OnboardingScreen]
    B -- Yes --> D{Session valid?}
    C --> E[AuthScreen]
    D -- No --> E
    D -- Yes --> F[Main Tabs]
    E --> G{Auth success?}
    G -- Yes --> F
    G -- No --> E
```

## Contracts
- `SessionCoordinator` is the single source of truth for onboarding + auth session state.
- `OnboardingViewModel` emits one-time effects for navigation.
- `AuthViewModel` handles validation, loading, error mapping, and success effect.
- Screens do not call repositories directly.

## Failure Handling
- Invalid credentials: map to inline field errors.
- Network error: show retry + non-blocking snackbar.
- Expired session: clear token and return to `auth/login`.

