# Aion UI Revamp Phase 2 Plan (Refined)

This document outlines the second phase of the UI revamp, focusing on deeper glassmorphism integration, refined animations, visibility improvements, and layout adjustments.

## Core Updates

### 1. Visibility & Backgrounds
- **Accent-Dependent Gradients**: Add a subtle radial or linear gradient to the background that depends on the current accent color. This ensures glass components have enough "texture" to be visible against pure black/white.
- **Pure Black Optimization**: Ensure high-contrast borders (using accent colors or white/black at low alpha) are used on all `GlassCard` instances.

### 2. Layout & Scrolling
- **Full-Screen Scrolling**: Update `HomeScreen` and others to extend their scrollable content behind the `GlassBottomBar`.
- **Top Bar Alignment**: Adjust the `AionHomeHeader` (Welcome section) to sit directly below the `AionTopAppBar` without excessive spacing.

### 3. Interaction Changes
- **Long-Press to Delete**: Remove the explicit delete button from `PlanAppCard`. Implement `combinedClickable` to trigger deletion via long-press.
- **Haptic Feedback**: Add subtle haptic feedback for long-press and interactive glass elements.

### 4. Component Revamp List

#### [GlassCard.kt](file:///mnt/sdb8/free-explore/android/aion/app/src/main/java/com/example/aion/ui/components/GlassCard.kt)
- Add optional "glow" or "inner shadow" parameters.
- Improve default border visibility.

#### [PlanAppCard.kt](file:///mnt/sdb8/free-explore/android/aion/app/src/main/java/com/example/aion/ui/components/PlanAppCard.kt)
- **Glass Progress Bar**: Replace `LinearProgressIndicator` with a custom glass-styled progress bar (blur + subtle glow).
- **Interaction**: Support `onLongClick`. Remove trash icon.

#### [AionStreakBar.kt](file:///mnt/sdb8/free-explore/android/aion/app/src/main/java/com/example/aion/ui/components/AionStreakBar.kt)
- **Glass Streak Items**: Make the individual day circles glass-styled with high-gloss surfaces.
- **Enhanced Animation**: Add a "glowing" effect to the "Today" item.

#### [AionStatCard.kt](file:///mnt/sdb8/free-explore/android/aion/app/src/main/java/com/example/aion/ui/components/AionStatCard.kt) & [AionProgressGauge.kt](file:///mnt/sdb8/free-explore/android/aion/app/src/main/java/com/example/aion/ui/components/AionProgressGauge.kt)
- **Liquid Animations**: Refine progress filling with custom easings (e.g., `FastOutSlowInEasing`).
- **Glass Gauges**: Add a subtle inner shadow/glow to the gauge tracks.

#### [AionLimitPicker.kt](file:///mnt/sdb8/free-explore/android/aion/app/src/main/java/com/example/aion/ui/components/AionLimitPicker.kt)
- **Glass Input Fields**: Convert numeric inputs to glass boxes with blurred backgrounds and high-contrast text.

#### [AddAppCard.kt](file:///mnt/sdb8/free-explore/android/aion/app/src/main/java/com/example/aion/ui/components/AddAppCard.kt)
- **Glass Layout**: Wrap the entire card in a `GlassCard`.
- **Glass Checkbox**: Custom selection indicator with a "liquid" fill animation.

#### [NotificationItem.kt](file:///mnt/sdb8/free-explore/android/aion/app/src/main/java/com/example/aion/ui/components/NotificationItem.kt)
- **Glass Item**: Convert to a `GlassCard` layout.

#### [AionHistoryItem.kt](file:///mnt/sdb8/free-explore/android/aion/app/src/main/java/com/example/aion/ui/components/AionHistoryItem.kt)
- **Glass History**: Use `GlassCard` for usage history list items.

#### [SortHeader.kt](file:///mnt/sdb8/free-explore/android/aion/app/src/main/java/com/example/aion/ui/components/SortHeader.kt)
- **Glass Sort**: Convert the sort action area to a small glass pill/button.

#### [AionActionButtons.kt](file:///mnt/sdb8/free-explore/android/aion/app/src/main/java/com/example/aion/ui/components/AionActionButtons.kt)
- **Glass Buttons**: high-gloss surfaces with high-contrast text and scale-on-press animations.

#### [AionToggleCard.kt](file:///mnt/sdb8/free-explore/android/aion/app/src/main/java/com/example/aion/ui/components/AionToggleCard.kt)
- **Glass Switch**: Revamp the `Switch` thumb and track for a more liquid feel.

#### [GlassBottomBar.kt](file:///mnt/sdb8/free-explore/android/aion/app/src/main/java/com/example/aion/ui/navigation/GlassBottomBar.kt)
- **Icon Glow**: Add a "glowing" backdrop specifically for the selected icon.

## Verification Plan
- **Build**: Ensure all components compile.
- **Visuals**: Check visibility on pure black backgrounds using gradients.
- **Scroll**: Verify `LazyColumn` content passes behind the navigation bar.
- **Interaction**: Confirm long-press delete works and is intuitive.
