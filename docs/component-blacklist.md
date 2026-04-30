# Component Blacklist

Items to revisit later for UDF/state-hoisting cleanup:

- `HomeScreen`: still owns sample app lists inline; move screen data into a hoisted state source or view model later.
- `PlanScreen`: still uses local sample data for cards; replace with real state and upstream data flow later.
- `SettingsScreen`: theme/accent selection state is still local to the screen; persist and hoist when settings storage exists.
- `AppDetailsScreen`: still owns tab and limit state internally; move to a state holder when the detail flow is connected to real data.
