package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var today: String
    private lateinit var lastDay: String
    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        getDates()
        viewModelScope.launch {
            try {
                asteroidsRepository.refreshAsteroids(
                    today, lastDay, application.resources.getString(
                        R.string.API_KEY
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    val asteroids = asteroidsRepository.asteroids


    private fun getDates() {

        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        today = dateFormat.format(currentTime)
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val lastTime = calendar.time
        lastDay = dateFormat.format(lastTime)
    }
}