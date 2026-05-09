# Aion UI Revamp Plan

This document outlines the planned UI changes for the Aion application to implement a "Liquid Glass" effect, pure black dark mode, and enhanced animations.

## Core Principles

1.  **Liquid Glass Effect**: Leverage the `backdrop` library to create glassmorphism components with blur and subtle color tints.
2.  **Pure Black Dark Mode**: Dark mode will use `#000000` as the background color for better OLED efficiency and aesthetic appeal.
3.  **Subtle Accents**: Accent colors will be used sparingly, primarily as tints in glass components and for interactive elements.
4.  **Fluid Animations**: Implement smooth transitions for navigation, tab switching, and data visualization.

## Component Updates

### 1. Theme and Colors (`/ui/theme/`)
*   **Dark Mode**: Update `DarkSurface` and `DarkBackground` to `#000000`.
*   **Accent Colors**: Define a set of subtle accent colors that work well with glass effects.
*   **Backdrop Integration**: Set up the necessary configurations for the `backdrop` library.

### 2. Navigation Components (`/ui/navigation/`, `MainActivity.kt`)
*   **Glass Bottom Bar**: Replace the standard `NavigationSuiteScaffold` navigation with a custom `Glass Bottom Bar` using the `backdrop` library.
*   **Highlight Animation**: Add a smooth, animated highlight for the selected navigation item.

### 3. Cards and Containers (`/ui/components/`)
*   **Glass Cards**: Update `AionStatCard`, `PlanAppCard`, `AddAppCard`, and `AionToggleCard` to use glassmorphism.
*   **Liquid Effect**: Apply a subtle liquid/flowing effect to the background of glass cards.
*   **Glass Search Bar**: Update `AionSearchBar` to a glass style.

### 4. Interactive Elements (`/ui/components/`)
*   **Glass Tabs**: Revamp `AionTabs` and `NotificationTabs` with a glass look and animated indicators.
*   **Glass Buttons**: Update `AionFilledButton` and other action buttons to incorporate glass effects.

### 5. Specific Component Animations
*   **Stats Component**: Add a "filling" animation for the progress indicators in `AionStatCard` and `AionProgressGauge`.
*   **Streak Component**: Implement a "pop" or "fade-in" animation for completed days in `AionStreakBar`.
*   **Switching Tabs**: Use `AnimatedContent` or `Pager` transitions for smooth tab switching.

## Screen-Specific Revamps

### 1. Home Screen
*   **Glass Header**: Update `AionHomeHeader` to a glass style.
*   **Animated Stats**: Stats cards should animate their values and progress when the screen is loaded.

### 2. Plan Screen
*   **Glass List Items**: Each app card in the plan list will be a glass component.
*   **Empty State**: Add a nice glass-themed empty state animation.

### 3. App Details Screen
*   **Glass Backdrop**: Use the app icon as a blurred backdrop for the header.
*   **Animated History**: Usage history items should fade in with a slight slide animation.

### 4. Profile Screen
*   **Glass Profile Section**: Update `ProfileComponents` to use glassmorphism.

## Technical Requirements
*   **Backdrop Library**: `implementation("io.github.kyant0:backdrop:1.0.6")` (or latest stable).
*   **Compose Animations**: Use `animate*AsState`, `AnimatedVisibility`, and `Transition` APIs.

## Next Steps
1.  Update dependencies in `build.gradle.kts`.
2.  Modify `Theme.kt` and `Color.kt` for pure black and accent colors.
3.  Implement a base `GlassCard` component.
4.  Iteratively update screens and components as detailed above.
