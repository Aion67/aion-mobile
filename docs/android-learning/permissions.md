# Permissions

## Permissions this app uses
The manifest declares these permissions in [AndroidManifest.xml](../../app/src/main/AndroidManifest.xml):
- `android.permission.PACKAGE_USAGE_STATS`
- `android.permission.POST_NOTIFICATIONS`
- `android.permission.SYSTEM_ALERT_WINDOW`
- `android.permission.QUERY_ALL_PACKAGES`

## What each permission is for
### Usage stats
`PACKAGE_USAGE_STATS` lets the app read app usage history from the system usage stats APIs.
This is the core permission for usage tracking and dashboard calculations.

### Notifications
`POST_NOTIFICATIONS` lets the app show notifications on Android 13 and above.
This matters when the app warns about limits, syncs, or app events.

### Overlay
`SYSTEM_ALERT_WINDOW` lets the app draw over other apps.
This is only needed if the app shows floating UI or overlay-based features.

### Package visibility
`QUERY_ALL_PACKAGES` lets the app see installed apps more broadly.
That is useful when listing apps the user may track.

## How permissions work here
This app uses two kinds of permission handling:
- manifest permissions declared up front
- runtime or settings-based approval checks

Not all Android permissions use the same request flow.

## Important Android detail
`PACKAGE_USAGE_STATS` is not requested with the normal runtime permission dialog.
The user must enable it in system settings.
See [PermissionUtils](../../app/src/main/java/com/example/aion/utils/PermissionUtils.kt).

`SYSTEM_ALERT_WINDOW` is also a special settings-based permission.

`POST_NOTIFICATIONS` is a runtime permission on newer Android versions.
The app should request it when it is actually needed.

## Permission helper behavior
[PermissionUtils](../../app/src/main/java/com/example/aion/utils/PermissionUtils.kt) checks:
- whether usage stats access is allowed
- whether overlay permission is allowed
- which settings intent to open for each case

## App behavior to teach a new developer
- If usage stats are missing, the app cannot build real usage dashboards.
- If notification permission is missing, alerts may not appear.
- If overlay permission is missing, any overlay feature must stay disabled.
- If package visibility is limited, app lists may be incomplete on some devices.

## Practical rule
Permissions are part of product behavior, not just setup.
Every feature that depends on system access must fail clearly when permission is missing.
