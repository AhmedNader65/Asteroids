package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.network.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidsRepository {

    suspend fun refreshAsteroids(startDate: String, endDate: String) {
        withContext(Dispatchers.IO) {
            val asteroids = Network.asteroids.getNeoWS(startDate,endDate).await()
        }
    }
}