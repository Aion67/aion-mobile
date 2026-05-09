package com.example.aion.data.manager

import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsageStatsHelper @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    fun getUsageStats(startTime: Long, endTime: Long) =
        usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)

    fun getAppUsageDuration(packageName: String, startTime: Long, endTime: Long): Long {
        val stats = getUsageStats(startTime, endTime)
        return stats?.find { it.packageName == packageName }?.totalTimeInForeground ?: 0L
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun getUsageEvents(startTime: Long, endTime: Long) =
        usageStatsManager.queryEvents(startTime, endTime)
}
