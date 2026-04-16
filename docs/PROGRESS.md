# Enaf Project Progress Tracker

## Project Overview
Implementing the "Enaf" wellness and productivity app in Jetpack Compose, based on the Figma "Celestial Pulse" design.

## Status Summary
- [x] Initial Project Setup
- [x] Navigation Structure (MainActivity refactor)
- [x] Design System (Colors, Theme)
- [ ] Components Library (In Progress)
- [ ] Onboarding Screen
- [ ] Auth Screens (Login/Sign Up)
- [ ] Home Dashboard
- [ ] Screen Time Planner
- [ ] Habit Insights
- [ ] Settings Screen
- [ ] Time's Up Overlay

---

## Detailed Task List

### 1. Core Architecture & Theme
- [x] Basic NavHost and NavigationSuiteScaffold setup.
- [x] Color palette extraction from Figma.
- [x] Theme integration (Glassmorphism foundations).
- [ ] Typography and Font implementation (Inter font family).

### 2. Common Components (`ui.components`)
- [x] `MotivationalCard`: The anti-procrastination card with fear reminder.
- [ ] `GlassCard`: Generic wrapper for glassmorphism effect.
- [ ] `BentoCard`: Base for different dashboard cards.
- [ ] `BottomNavBar`: Custom icons and active states.
- [ ] `PulseButton`: Primary CTA button with glow.

### 3. Home Dashboard (`ui.screens.home`)
- [ ] **Hero Section**: Daily Summary Card (Bento Layout).
- [ ] **Habits Section**: Today's Habits asymmetric list.
- [ ] **Motivational Card**: (Implemented).

### 4. Screen Time Planner (`ui.screens.planner`)
- [ ] **Global Limit Card**: Slider and usage indicators.
- [ ] **App Limit List**: Bento style grid for apps like TikTok, YouTube.

### 5. Habit Insights (`ui.screens.insights`)
- [ ] **Monthly Streak Hero**.
- [ ] **Average Completion Graph**.
- [ ] **Category Distribution Donut Chart**.

---

## Technical Debt / Notes
- [ ] Implement `renderEffect` for high-quality blur on Android 12+.
- [ ] Setup Inter font family as used in Figma.
- [ ] Data layer needs to be defined (Room/DataStore).
