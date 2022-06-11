package com.udacity.asteroidradar.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Domain objects are plain Kotlin data classes that represent the things in our app. These are the
 * objects that should be displayed on screen, or manipulated by the app.
 */

@Parcelize
@Entity
data class Asteroid(
    @PrimaryKey
    val id: Long,
    val codename: String, val closeApproachDate: String,
    val absoluteMagnitude: Double, val estimatedDiameter: Double,
    val relativeVelocity: Double, val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
) : Parcelable{

    fun isPotentiallyHazardousDescription():String{
        return "This asteroid is ${if (!isPotentiallyHazardous) "not" else "" } potentially hazardous"
    }
}

data class POD(
    @Json(name = "url")
    val url: String,
    @Json(name = "title")
    val title: String
)
