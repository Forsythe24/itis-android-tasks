package com.itis.itis_android_tasks.utils.worker

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.itis.itis_android_tasks.di.ServiceLocator
import com.itis.itis_android_tasks.utils.ParamsKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalDate

class UserDeletionWorker(
    private val context: Context,
    private val params: WorkerParameters
): CoroutineWorker(context, params) {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        return (withContext(Dispatchers.IO) {

            inputData.getString(ParamsKey.USER_ID_KEY)?.let {

                val db = ServiceLocator.getDbInstance()

                db.userDao.deleteUserById(it)
                db.userBookDao.deleteAllUserFavorites(it)
            }

        } ?: Result.failure()) as Result
    }
}
