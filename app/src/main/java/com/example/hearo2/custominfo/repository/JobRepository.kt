package com.example.hearo2.custominfo.repository

import android.content.Context
import com.example.hearo2.custominfo.model.JobDetailData
import com.example.hearo2.custominfo.model.JobItem
import com.example.hearo2.network.api.JobApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JobRepository {

    suspend fun getJobList(context: Context): List<JobItem> =
        withContext(Dispatchers.IO) {
            val response = JobApi.getService(context).getJobList()
            response.data.items
        }

    suspend fun searchJobs(context: Context, filters: Map<String, String>): List<JobItem> =
        withContext(Dispatchers.IO) {
            val response = JobApi.getService(context).searchJobs(filters)
            response.data.items
        }

    suspend fun getJobDetail(context: Context, rno: String): JobDetailData =
        withContext(Dispatchers.IO) {
            val response = JobApi.getService(context).getJobDetail(rno)
            response.data
        }
}
