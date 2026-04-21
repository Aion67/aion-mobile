# Data and Storage Design (Target)

## Data Sources
```mermaid
flowchart TD
    A[System Usage Stats API] --> R[UsageRepository]
    B[Remote Profile/Auth API] --> AR[AuthRepository]
    C[Room Database] --> R
    D[DataStore Preferences] --> SR[SettingsRepository]

    R --> VM1[HomeViewModel]
    R --> VM2[PlannerViewModel]
    R --> VM3[InsightsViewModel]
    SR --> VM4[SettingsViewModel]
    AR --> VM5[AuthViewModel]
```

## Core Entities
```mermaid
classDiagram
    class UserProfile {
      userId: String
      email: String
      displayName: String
      tier: String
    }
    class AppUsageRecord {
      packageName: String
      date: LocalDate
      usedMinutes: Int
      limitMinutes: Int
      category: String
    }
    class DailySummary {
      date: LocalDate
      completionRate: Float
      focusMinutes: Int
    }
    class Settings {
      quietModeHours: Int
      regretSimulationEnabled: Boolean
      opportunityLeakEnabled: Boolean
      themeMode: String
    }
```

## Storage Responsibilities
- Room stores historical usage, streak snapshots, and aggregated analytics caches.
- DataStore stores user preferences, theme mode, onboarding completion, and lightweight flags.
- Auth token is stored securely (EncryptedSharedPreferences or platform-secure equivalent).

## Sync Strategy
- Pull latest usage summaries on app open and periodic refresh.
- Use local-first reads for UI speed; merge remote updates asynchronously.
- Keep deterministic conflict resolution: latest timestamp wins for mutable profile/preferences.

