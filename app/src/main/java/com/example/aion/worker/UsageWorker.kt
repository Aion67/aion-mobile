package com.example.aion.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.aion.data.entities.UsageSessionEntity
import com.example.aion.data.entities.NotificationEntity
import com.example.aion.data.entities.NotificationType
import com.example.aion.data.manager.UsageStatsHelper
import com.example.aion.data.manager.AionNotificationManager
import com.example.aion.data.repository.AppRepository
import com.example.aion.data.repository.UsageRepository
import com.example.aion.data.repository.NotificationRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.util.Calendar

@HiltWorker
class UsageWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val appRepository: AppRepository,
    private val usageRepository: UsageRepository,
    private val notificationRepository: NotificationRepository,
    private val usageStatsHelper: UsageStatsHelper,
    private val notificationManager: AionNotificationManager
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startTime = calendar.timeInMillis

        val trackedApps = appRepository.getAllTrackedApps().first()

        for (app in trackedApps) {
            val duration = usageStatsHelper.getAppUsageDuration(app.packageName, startTime, endTime)
            if (duration > 0) {
                // Log session
                usageRepository.logUsageSession(
                    UsageSessionEntity(
                        appPackageName = app.packageName,
                        startTime = startTime,
                        endTime = endTime,
                        totalDurationMs = duration
                    )
                )

                // Check limits
                val settings = appRepository.getSettingsForApp(app.packageName).first()
                if (settings != null && settings.isTracked && settings.dailyLimitMs > 0) {
                    if (duration >= settings.dailyLimitMs && settings.notifyOnLimit) {
                        triggerLimitNotification(app.appName, app.packageName, "You've reached your daily limit for ${app.appName}!")
                    } else if (duration >= settings.dailyLimitMs / 2 && settings.notifyOnHalfLimit) {
                         // Logic to prevent spamming half-limit notification could be added here
                        triggerLimitNotification(app.appName, app.packageName, "You've used half of your daily limit for ${app.appName}.", NotificationType.HALF_LIMIT_REACHED)
                    }
                }
            }
        }

        return Result.success()
    }

    private suspend fun triggerLimitNotification(
        appName: String,
        packageName: String,
        message: String,
        type: NotificationType = NotificationType.LIMIT_REACHED
    ) {
        notificationManager.showLimitAlert(appName, message)
        notificationRepository.addNotification(
            NotificationEntity(
                title = "Limit Alert: $appName",
                message = message,
                appPackageName = packageName,
                type = type
            )
        )
    }
}
