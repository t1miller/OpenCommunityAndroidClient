package rr.opencommunity.map

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import rr.opencommunity.home.map.model.Thread
import timber.log.Timber

class MapsViewModel : ViewModel() {

    var thread: MutableLiveData<Thread?> = MutableLiveData()

    private var currentLocation: Location? = null

    private var threadLocation: LatLng? = null


    fun uploadThread(t: Thread){
        Timber.d("uploading thread %s", t.message)
        thread.postValue(t)
        // maybe add thread to list of threads queued to uploaded
        // todo upload to server
    }

    fun updateCurrentLocation(location: Location?) {
        currentLocation = location
    }

    fun updateThreadLocation(location: LatLng?) {
        threadLocation = location
    }

    fun getThreadLocation() : LatLng {
        return threadLocation ?: LatLng(0.0, 0.0)
    }

    fun getLastLocation() : Location? {
        return currentLocation
    }

    fun currentLocationAsLatLng(): LatLng {
        val lat = currentLocation?.latitude
        val lng = currentLocation?.longitude
        return LatLng(lat ?: 0.0,lng ?: 0.0)
    }
}
