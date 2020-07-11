package rr.opencommunity.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources.NotFoundException
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import rr.opencommunity.R
import rr.opencommunity.home.map.model.Thread
import rr.opencommunity.login.ui.LoginActivity
import rr.opencommunity.settings.ApiTestingActivity
import rr.opencommunity.settings.SettingsActivity
import rr.opencommunity.social.UserActivity
import timber.log.Timber



class MapsActivity : AppCompatActivity(),
    OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener,
    GoogleMap.OnMapClickListener,
    LocationListener,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mMap: GoogleMap

    private lateinit var locationManager: LocationManager

    private lateinit var viewModel: MapsViewModel

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        enableMyLocation()

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10f, this)
        viewModel = ViewModelProviders.of(this).get(MapsViewModel::class.java)

        // Create the observer which updates the UI.
        val threadObserver = Observer<Thread?> { thread ->
            val markerOptions = MarkerOptions()
                .position(thread?.gpsCoordinates ?: LatLng(0.0, 0.0))
                .title(thread?.message)
                .snippet(thread?.toString())
            val marker = mMap.addMarker(markerOptions)
            marker.showInfoWindow()
        }
        viewModel.thread.observe(this, threadObserver)

        val fab: View = findViewById(R.id.extendedFab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Create new thread", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()

            // Set this phones location as new thread creation location
            viewModel.updateThreadLocation(viewModel.currentLocationAsLatLng())
            loadNewThreadDialog()
        }

        // Setup the Nav Drawer
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.style_json
                )
            )
            if (!success) {
                Timber.e("Style parsing failed.")
            }
        } catch (e: NotFoundException) {
            Timber.e(e)
        }
        googleMap.isMyLocationEnabled = true

        // Set zoomable range
        googleMap.setMinZoomPreference(1f)
        googleMap.setMinZoomPreference(10f)

        // Set button listeners
        googleMap.setOnMyLocationButtonClickListener(this)
        googleMap.setOnMyLocationClickListener(this)
        googleMap.setOnMapClickListener(this)
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Timber.d("on nav item selected %s", item.alphabeticShortcut)

        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_login -> {
                // goes to boiler plate login activity
                val loginIntent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(loginIntent)
            }
            R.id.nav_friends -> {
                val userActivity = Intent(applicationContext, UserActivity::class.java)
                startActivity(userActivity)
            }
            R.id.nav_api_testing -> {
                val apiTestActivity = Intent(applicationContext, ApiTestingActivity::class.java)
                startActivity(apiTestActivity)
            }
            R.id.nav_settings -> {
                val settingsActivity = Intent(applicationContext, SettingsActivity::class.java)
                startActivity(settingsActivity)
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show()
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(this, "Current location:\n$p0", Toast.LENGTH_LONG).show()
    }

    fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Timber.d("Already have LOCATION permission")
        } else {
            // You can directly ask for the permission.
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Timber.d("User gave permission")
                } else {
                    Timber.d("User did not give permission")
                }
            }
        }
    }

    override fun onLocationChanged(location: Location?) {
        viewModel.updateCurrentLocation(location)
        val latLng = if (location == null) LatLng(0.0,0.0) else LatLng(location.latitude, location.longitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16f))
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Timber.d("on status changed")
    }

    override fun onProviderEnabled(provider: String?) {
        Timber.d("on provider enabled")
    }

    override fun onProviderDisabled(provider: String?) {
        Timber.d("on provider disabled")
    }

    override fun onMapClick(p0: LatLng?) {
        Timber.d("map was clicked, gps coord: %s", p0)
        viewModel.updateThreadLocation(p0)
        loadNewThreadDialog()
    }

    private fun loadNewThreadDialog() {
        // load new thread frag
        val fragMan = supportFragmentManager
        val dialog = NewThreadDialog.newInstance()
        dialog.show(fragMan, dialog.tag)
    }

    companion object {
        /**
         * Request code for location permission request.
         *
         * @see .onRequestPermissionsResult
         */
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }


}
