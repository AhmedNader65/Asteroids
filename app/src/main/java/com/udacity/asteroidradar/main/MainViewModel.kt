package com.udacity.asteroidradar.main

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.BuildConfig.NASA_API_KEY
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.POD
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

private const val URL_KEY: String = "POD_URL"
private const val IMAGE_DESC: String = "POD_DESC"

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var today: String
    private lateinit var lastDay: String
    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)
    var sharedPreferences: SharedPreferences =
        application.getSharedPreferences("MySharedPref", MODE_PRIVATE)
    var myEdit = sharedPreferences.edit()

    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        refreshData()
    }

    fun refreshData() {

        getDates()
        viewModelScope.launch {
            val apiKey = NASA_API_KEY
            try {
                asteroidsRepository.refreshAsteroids(
                    today, lastDay, apiKey
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                asteroidsRepository.getPOD(apiKey)

            } catch (e: Exception) {
                sharedPreferences.getString(URL_KEY,"")?.let {
                    asteroidsRepository.getCachedPOD(it,sharedPreferences.getString(IMAGE_DESC,"")!!)  }
            }
        }
    }


    val asteroids = asteroidsRepository.asteroids
    val pod = asteroidsRepository.pod

    fun cachePOD(pod: POD){
        myEdit.putString(URL_KEY,pod.url)
        myEdit.putString(IMAGE_DESC,pod.title)
        myEdit.apply()
    }
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