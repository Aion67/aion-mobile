package com.example.aion.util

object ScoringEngine {
    /**
     * Calculates a score from 0 to 100 based on usage vs limit.
     */
    fun calculateAppScore(usageMs: Long, limitMs: Long): Float {
        if (limitMs <= 0L) return 0f
        if (usageMs == 0L) return 100f
        
        val ratio = usageMs.toFloat() / limitMs
        return (100f * (1f - ratio)).coerceIn(0f, 100f)
    }

    /**
     * Calculates improvement percentage.
     */
    fun calculateImprovement(pastMs: Long, currentMs: Long): Float {
        if (pastMs > 0L) {
            return (pastMs - currentMs).toFloat() / pastMs
        } else if (currentMs == 0L) {
            return 0f
        }
        return -1f // Regression
    }
}
