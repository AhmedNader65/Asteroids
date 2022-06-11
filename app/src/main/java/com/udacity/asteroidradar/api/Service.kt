package com.udacity.asteroidradar.network

import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.domain.Asteroid
import kotlinx.coroutines.Deferred
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * A retrofit service to fetch NASA data.
 */
interface NeoWSService {
    @GET("feed")
    suspend fun getNeoWS(
        @Query("start_date") start_date :String,
        @Query("end_date") end_date :String,
        @Query("api_key") apiKey :String,
    ): String
}

/**
 * Main entry point for network access. Call like `Network.devbytes.getPlaylist()`
 */
object Network {
    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    val asteroids = retrofit.create(NeoWSService::class.java)
}
