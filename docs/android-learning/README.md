# Aion Android Learning Guide

This folder explains the app from top to bottom: UI, state, navigation, data, Room, permissions, and the work scheduler.

## What this app is built with
- Jetpack Compose for UI
- ViewModel and StateFlow for screen state
- Hilt for dependency injection
- Room for local persistence
- WorkManager for background sync
- Android system permissions for usage tracking, notifications, overlays, and app visibility

## Reading order
1. [UI and Compose](./ui-and-compose.md)
2. [ViewModels and state](./viewmodels-and-state.md)
3. [Data layer and Room](./data-layer-and-room.md)
4. [Permissions](./permissions.md)
5. [Navigation and app flow](./navigation-and-flow.md)
6. [Code map](./code-map.md)

## App-level summary
The app shows dashboard data about phone usage, tracked apps, limits, notifications, and settings.

The flow is simple:
- Compose screens render UI.
- ViewModels assemble the state for each screen.
- Repositories hide database and system-access details.
- Room stores tracked apps, usage sessions, notifications, profiles, preferences, and future feature data.
- Permission helpers check whether the app can read usage stats or draw overlays.
- WorkManager keeps the usage sync process running in the background.
