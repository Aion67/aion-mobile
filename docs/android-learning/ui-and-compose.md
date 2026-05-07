# UI and Compose

## What Compose does here
Jetpack Compose is the presentation layer. Each screen is a `@Composable` function that describes the UI for that screen.

Examples in this app:
- [HomeScreen](../../app/src/main/java/com/example/aion/ui/screens/HomeScreen.kt)
- [PlanScreen](../../app/src/main/java/com/example/aion/ui/screens/PlanScreen.kt)
- [NotificationsScreen](../../app/src/main/java/com/example/aion/ui/screens/NotificationsScreen.kt)
- [SettingsScreen](../../app/src/main/java/com/example/aion/ui/screens/SettingsScreen.kt)

## Composable basics
A composable is a function that can emit UI. In this app, composables are mostly responsible for:
- laying out content with `Scaffold`, `LazyColumn`, `Row`, `Column`, and `Box`
- showing reusable UI blocks like cards, headers, tabs, and buttons
- reacting to state exposed by ViewModels

## UI structure in Aion
The app is organized around small reusable components in [ui/components](../../app/src/main/java/com/example/aion/ui/components).
Examples:
- [AionTopAppBar](../../app/src/main/java/com/example/aion/ui/components/AionTopAppBar.kt)
- [AionProgressGauge](../../app/src/main/java/com/example/aion/ui/components/AionProgressGauge.kt)
- [PlanAppCard](../../app/src/main/java/com/example/aion/ui/components/PlanAppCard.kt)
- [AionStatCard](../../app/src/main/java/com/example/aion/ui/components/AionStatCard.kt)
- [AionThemeOption](../../app/src/main/java/com/example/aion/ui/components/AionThemeOption.kt)

## Data classes in the UI layer
Compose screens often use simple data classes to package state.
Examples from this app:
- `HomeUiState`
- `TrackedAppUsage`
- other screen-specific state models in ViewModels

These are not database entities. They are UI state objects.

## Why components are split up
The app separates screens from reusable components so the same visual building blocks can be reused across features. That keeps the UI smaller, easier to test, and easier to change.

## What to remember
- Composables draw UI.
- They should not contain database logic.
- They should read state and send user actions upward.
- Reusable components should stay dumb and focused on rendering.
