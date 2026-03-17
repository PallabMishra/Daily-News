package com.kmpnewsapp

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kmpnewsapp.data.repository.NewsRepositoryImpl
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val repository: NewsRepositoryImpl by inject()

    override suspend fun doWork(): Result {
        return try {
            repository.syncHeadlines()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
