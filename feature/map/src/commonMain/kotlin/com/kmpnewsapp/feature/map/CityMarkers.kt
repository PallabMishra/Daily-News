package com.kmpnewsapp.feature.map

import com.swmansion.kmpmaps.core.Coordinates
import com.swmansion.kmpmaps.core.Marker

fun getCityMarkers(): List<Marker> {
    return INDIAN_CITIES.map { city ->
        Marker(
            coordinates = Coordinates(latitude = city.latitude, longitude = city.longitude),
            title = city.name,
            contentId = city.name
        )
    }
}
