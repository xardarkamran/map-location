package com.example.maps
import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import java.util.*
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private  var longitude:Double = 0.0
    private  var lattitude:Double = 0.0
    private lateinit var currentLocation:Location
    private lateinit var fusedlocationproviderclient:FusedLocationProviderClient
    private val REQUEST_CODE:Int=101
    /* private var mMap: GoogleMap? = null
     var mLastLocation: Location? = null
     var mCurrLocationMarker: Marker? = null
     var mGoogleApiClient: GoogleApiClient? = null
     var mLocationRequest: LocationRequest? = null*/

    private lateinit var addLagLat:List<LatLng>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        /*val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)*/
        fusedlocationproviderclient= LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()
       /* mMap.setOnMapClickListener(OnMapClickListener { point ->
            Toast.makeText(this,
                    point.latitude.toString() + ", " + point.longitude,
                    Toast.LENGTH_SHORT).show()
        })*/
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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
       /* longitude= 30.3753
        lattitude= 69.3451*/
        // Add a marker in Sydney and move the camera
       /* val sydney = LatLng(longitude, lattitude)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in pakistan"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        Toast.makeText(applicationContext, ""+currentLocation.latitude+ " " +currentLocation.longitude, Toast.LENGTH_LONG).show()

         //addLagLat.indexOf()
        val markerOptions = MarkerOptions().position(latLng).title("Current location")
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
        googleMap.addMarker(markerOptions)

        mMap.setOnMapClickListener(OnMapClickListener
        { point -> //myMap.addMarker(new MarkerOptions().position(point).title(point.toString()));
            //The code below demonstrate how to convert between LatLng and Location
            //Convert LatLng to Location
            val location = Location("Test")
            location.latitude = point.latitude
            location.longitude = point.longitude
            Toast.makeText(applicationContext, ""+location.latitude+ " " +location.longitude, Toast.LENGTH_LONG).show()


            //location.time = Date().time //Set time as current Date
            // txtinfo.setText(location.toString())
            //Toast.makeText(applicationContext, location.latitude.toString() + "" + location.longitude.toString(), Toast.LENGTH_SHORT).show()
            //Convert Location to LatLng
            val newLatLng = LatLng(location.latitude, location.longitude)
            val markerOptions = MarkerOptions().position(newLatLng).title(newLatLng.toString()+"  New Location")
            mMap.addMarker(markerOptions)
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            //val address: String = addresses[0].getAddressLine(0)

            if (addresses.size>0){
                if (addresses[0].getLocality()!=null && addresses[0].getAdminArea() != null && addresses[0].getCountryName()!=null) {
                    val city: String = addresses[0].getLocality()
                    val state: String = addresses[0].getAdminArea()
                    //val zip: String = addresses[0].getPostalCode()
                    val country: String = addresses[0].getCountryName()
                    //val street: String = addresses[0].subLocality
                    Toast.makeText(applicationContext, country + "   " + state + "   " + city + "   ", Toast.LENGTH_LONG).show()
                }
                else
                {
                    Toast.makeText(applicationContext,   " No More Detail  ", Toast.LENGTH_LONG).show()
                }
            }
        })



    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
            return
        }
        val task: Task<Location> = fusedlocationproviderclient.getLastLocation()
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
               // Toast.makeText(applicationContext, currentLocation.latitude.toString() + "" + currentLocation.longitude, Toast.LENGTH_SHORT).show()
                val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.mapnewid) as SupportMapFragment?)!!
                supportMapFragment.getMapAsync(this)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }
}