package com.example.schedule.data.network

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("upload/medialibrary/{link_part}")
    suspend fun downloadSchedule(@Path("link_part", encoded = true) linkPart: String): Response<ResponseBody>

    companion object{
        const val COURSE_3_LINK = "23d/23d374709ecb41b26010a5752662854d.xlsx"
        const val COURSE_4_LINK = "5b3/5b3f062a46da16ff335378b075793f6a.xlsx"
        const val COURSE_5_LINK = "36f/36fa784eb860c6d1e5c344fb2aae2145.xlsx"
    }

}