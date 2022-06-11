package com.udacity.asteroidradar.network

import android.provider.Telephony.TextBasedSmsColumns.BODY
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * A retrofit service to fetch NASA data.
 */
interface NeoWSService {
    @GET("neo/rest/v1/feed")
    suspend fun getNeoWS(
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String,
        @Query("api_key") apiKey: String,
    ): String

    @GET("planetary/apod")
    suspend fun getPOD(
        @Query("api_key") apiKey: String,
    ): String
}

/**
 * Main entry point for network access. Call like `Network.devbytes.getPlaylist()`
 */
object Network {
    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(getClient())
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private fun getClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
        }
        return httpClient.build()
    }

    val asteroids = retrofit.create(NeoWSService::class.java)
}
