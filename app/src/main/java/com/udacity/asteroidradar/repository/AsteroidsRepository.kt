package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.POD
import com.udacity.asteroidradar.network.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(private val database: AsteroidDatabase) {


    /**
     * A list of asteroids that can be shown on the screen.
     */
    val asteroids: LiveData<List<Asteroid>> = database.asteroidDao.getAsteroids()
    private val _pod = MutableLiveData<POD>()
    val pod: LiveData<POD> = _pod

    /**
     * Refresh the asteroids stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     */
    suspend fun refreshAsteroids(startDate: String, endDate: String, apiKey: String) {
        withContext(Dispatchers.IO) {
            val asteroids =
                parseAsteroidsJsonResult(
                    JSONObject(
                        Network.asteroids.getNeoWS(
                            startDate,
                            endDate,
                            apiKey
                        )
                    )
                )
            database.asteroidDao.delete()
            database.asteroidDao.insertAll(asteroids)
        }
    }

    /**
     * GET NASA Picture of the day.
     *
     * This function uses the IO dispatcher to ensure the server calling operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     */
    suspend fun getPOD(apiKey: String) {
        val result = withContext(Dispatchers.IO) {
             return@withContext Network.asteroids.getPOD(apiKey)
        }
        result.let {
            _pod.value = it
        }
    }

    fun getCachedPOD(it: String, desc: String) {
        _pod.value = POD(it,desc)
    }
}