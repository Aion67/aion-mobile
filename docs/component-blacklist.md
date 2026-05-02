# Component Blacklist

Items to revisit later for UDF/state-hoisting cleanup:

- `SettingsScreen`: theme/accent selection state is still local to the screen; persist and hoist when settings storage exists.
- `AppDetailsScreen`: still owns tab and limit state internally; move to a state holder when the detail flow is connected to real data.
- `ProfileScreen`: username and profile picture state are still local to the screen; hoist once profile persistence exists.
