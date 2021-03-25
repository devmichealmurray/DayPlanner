package com.devmmurray.dayplanner.util

import android.app.Application
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.devmmurray.dayplanner.ui.viewmodel.HomeViewModel

/**
 * Worker that deletes old events from the database
 */

class CleanDbWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }


    override suspend fun doWork(): Result {
        val todayMillis = System.currentTimeMillis()

        return try {
            HomeViewModel(Application()).deleteOldEvents(todayMillis)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }


}