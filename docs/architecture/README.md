# Enaf Architecture Documentation (Target State)

This folder defines the target architecture for building Enaf from UI prototype into a production-ready app.

## Reading Order
1. `00-system-architecture.md` (master system blueprint)
2. `01-target-state-overview.md`
3. `02-onboarding-auth-flow.md`
4. `03-viewmodel-design.md`
5. `04-data-and-storage-design.md`
6. `05-testing-strategy.md`
7. `per-file/MainActivity.md`
8. `per-file/OnboardingScreen.md`
9. `per-file/AuthScreen.md`
10. `per-file/HomeScreen.md`
11. `per-file/PlannerScreen.md`
12. `per-file/InsightsScreen.md`
13. `per-file/SettingsScreen.md`

## Documentation Rules
- Diagrams represent **target state**, even if implementation is still partial.
- Every screen file has its own design file under `per-file/`.
- Mermaid is used for architecture, sequence, and state diagrams.
- Keep docs updated whenever contracts change (`UiState`, repository APIs, navigation routes).
