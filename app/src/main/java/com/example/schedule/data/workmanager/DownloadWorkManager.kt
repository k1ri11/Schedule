package com.example.schedule.data.workmanager

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.work.*
import com.example.schedule.data.network.Api
import com.example.schedule.data.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class DownloadWorkManager(
    private val context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val linkPart = inputData.getString("urlPart")!!
        val course = inputData.getString("course")
        val response = RetrofitInstance.getApi().downloadSchedule(linkPart)
        response.body()?.let { body ->
            return withContext(Dispatchers.IO) {
                val file = File(context.filesDir, "downloaded_course_$course.xlsx")
                val outputStream = FileOutputStream(file)
                outputStream.use { stream ->
                    try {
                        stream.write(body.bytes())
                    } catch (e: IOException) {
                        return@withContext Result.failure(
                            workDataOf(
                                WorkerKeys.ERROR_MSG to e.localizedMessage
                            )
                        )
                    }
                }
                Result.success(
                    workDataOf(
                        WorkerKeys.IMAGE_URI to file.canonicalPath
                    )
                )
            }
        }
        return Result.failure(
            workDataOf(WorkerKeys.ERROR_MSG to "Unknown error")
        )
    }


    companion object {

        private fun getConstraints(): Constraints {
            return Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        }

        private fun createOneTimeWorkRequest(course: Int): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<DownloadWorkManager>()
                .setConstraints(getConstraints())
                .setInputData(setData(course))
                .build()
        }

        private fun setData(course: Int): Data {
            val data = Data.Builder()
            when (course) {
                3 -> data.putString("urlPart", Api.COURSE_3_LINK)
                4 -> data.putString("urlPart", Api.COURSE_4_LINK)
                5 -> data.putString("urlPart", Api.COURSE_5_LINK)
            }
            data.putString("course", course.toString())
            return data.build()
        }

        private const val WORK_NAME = "download_workManager"
        fun Context.startWorker(course: Int): Pair<LiveData<MutableList<WorkInfo>>, UUID> {
            val workManager = WorkManager.getInstance(this)
            val workInfo = workManager.getWorkInfosForUniqueWorkLiveData(WORK_NAME)
            val downloadRequest = createOneTimeWorkRequest(course)
            workManager.beginUniqueWork(
                WORK_NAME,
                ExistingWorkPolicy.KEEP,
                downloadRequest
            ).enqueue()
            return Pair(workInfo, downloadRequest.id)
        }
    }
}