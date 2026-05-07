# Navigation and App Flow

## Where navigation starts
The app entry point is [MainActivity](../../app/src/main/java/com/example/aion/MainActivity.kt).
It sets the theme, starts background work, and hosts the Compose navigation graph.

## Navigation model
The app uses Compose Navigation with route strings defined in [AionNavigation](../../app/src/main/java/com/example/aion/ui/navigation/AionNavigation.kt).

Current screens include:
- Home
- Plan
- Add Apps
- Notifications
- Settings
- Profile
- App Details
- Notification Details
- settings sub-pages

## Flow summary
1. MainActivity creates the app shell.
2. Navigation shows the current screen.
3. Each screen gets a ViewModel where needed.
4. User actions move between routes or update state.

## Background work
[WorkScheduler](../../app/src/main/java/com/example/aion/data/manager/WorkScheduler.kt) is triggered from MainActivity.
That keeps usage sync logic alive outside the UI.

## Why this matters
This app is not just a set of isolated screens.
It is a layered flow:
- screens render
- ViewModels decide state
- repositories read and write data
- background workers keep data fresh
