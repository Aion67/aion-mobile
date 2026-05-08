# ViewModels and State

## What a ViewModel does
A ViewModel prepares screen state for Compose.
It sits between the UI and the data layer.

Examples in this app:
- [HomeViewModel](../../app/src/main/java/com/example/aion/ui/viewmodels/HomeViewModel.kt)
- [PlanViewModel](../../app/src/main/java/com/example/aion/ui/viewmodels/PlanViewModel.kt)
- [NotificationsViewModel](../../app/src/main/java/com/example/aion/ui/viewmodels/NotificationsViewModel.kt)
- [SettingsViewModel](../../app/src/main/java/com/example/aion/ui/viewmodels/SettingsViewModel.kt)

## Why this app uses ViewModels
The app uses ViewModels to:
- survive configuration changes
- keep business logic out of the UI
- combine data from repositories
- expose one screen state object to Compose

## StateFlow in this app
The ViewModels expose `StateFlow` objects that Compose collects.
That gives the UI an always-current state stream.

In [HomeViewModel](../../app/src/main/java/com/example/aion/ui/viewmodels/HomeViewModel.kt):
- the ViewModel combines tracked apps, user profile, usage data, and settings
- it transforms those raw values into `HomeUiState`
- the screen reads that state and renders cards, gauges, and stats

## Typical flow
1. Compose screen asks for a ViewModel.
2. ViewModel reads from repositories.
3. ViewModel transforms the data into a UI state model.
4. Compose observes the state and redraws when it changes.

## Why the Home screen is a good example
[HomeScreen](../../app/src/main/java/com/example/aion/ui/screens/HomeScreen.kt) does very little itself.
It reads `uiState` and renders the dashboard.
All usage math, rank calculation, and state combination happen in the ViewModel.

## What a ViewModel should not do
- render UI
- hold Android permissions UI flows directly unless necessary
- talk to Room tables directly
- contain one-off Compose layout code

## Practical rule
If logic decides what the screen should show, it belongs in the ViewModel or below it, not inside the composable.
