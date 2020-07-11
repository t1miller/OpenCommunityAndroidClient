package rr.opencommunity.home.map.model

import com.google.android.gms.maps.model.LatLng

data class Thread(
    val radius: Int,
    val gpsCoordinates: LatLng,
    val isGpsExact: Boolean,
    val isPostPublic: Boolean,
    val isInfoType: Boolean,
    val expirationHours: Int,
    val message: String
)