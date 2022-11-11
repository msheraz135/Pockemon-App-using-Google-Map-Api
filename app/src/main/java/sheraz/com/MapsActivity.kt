package sheraz.com

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import sheraz.com.databinding.ActivityMapsBinding


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

/**
todo: Global Variable
*/

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

/**todo: oncreate funtion starts here*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
         // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

         checkPermmision()
        loadPockemon()
    }

    /**
     *
     * todo: getting permisions to access location
     * todo: //all location fun open from here {
     *
     * */

    fun checkPermmision()
    {
        if(Build.VERSION.SDK_INT>=23)
        {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),MY_PERMISSIONS_REQUEST_LOCATION_CODE)
                return
            }
        }
        GetUserLocation()
    }
    @SuppressLint("MissingPermission")
    fun GetUserLocation()
    {
        Toast.makeText(this,"user locatiom access on",Toast.LENGTH_SHORT).show()
        //TODO:  / do latter
        var myLocation=MyLocationListner()
        var locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        //        system services contain calentder/etc
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,myLocation)
        var mythread=myThread()
        mythread.start()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode)
        {
            MY_PERMISSIONS_REQUEST_LOCATION_CODE->{
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    GetUserLocation()
                }
                else
                {
                    Toast.makeText(this,"Permisiion not granted",Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * } //
     * todo: getting permisions to acces location
     * all location fun closed here
     *
     * */









    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *
     * key:AIzaSyDnUfKA0Yq32PzQT9PyXk5YtDk1gx7E3go
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

    }

    //get user Location
    var location:Location?= null
    inner class MyLocationListner() :LocationListener {
        init {
            location= Location("Start")
            location!!.latitude=0.0
            location!!.longitude=0.0
        }
        override fun onLocationChanged(p0: Location) {
//            TODO("Not yet implemented")
            location=p0
        }
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
//            super.onStatusChanged(provider, status, extras)
        }
        override fun onProviderEnabled( provider: String) {
//            super.onProviderEnabled(provider)
        }
        override fun onProviderDisabled( provider: String) {
//            super.onProviderDisabled(provider)
        }
    }

    var oldLocation:Location?=null

    inner class myThread() : Thread() {
        init {
            oldLocation= Location("Start")
            oldLocation!!.latitude=0.0
            oldLocation!!.longitude=0.0
        }

        override fun run() {
            while(true)
            {
                try {
                    if(oldLocation!!.distanceTo(location)==0f)
                    {
                        continue
                    }
                    oldLocation=location
                    runOnUiThread {
                        mMap!!.clear()
                        val icon : BitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.mario)

                        // Add a marker in Sydney and move the camera // show me
                        val sydney = LatLng(location!!.latitude, location!!.longitude)
                        mMap.addMarker(MarkerOptions()
                            .position(sydney)
                            .title("Me")
                            .snippet("here is my picture")
                            .icon(icon))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 20f))
                        // show pokemon

                        for (i in 0 until listPockemons.size)
                        {
                            var getPockemon = listPockemons[i]
                          if(!getPockemon.isCatch)
                          {
                              //val pokemonLocationSet = LatLng(getPockemon.location!!.latitude,getPockemon.location!!.longitude)
                              val pokemonLocationSet = LatLng(getPockemon.lat!!,getPockemon.log!!)
                              mMap!!.addMarker(MarkerOptions()
                                  .position(pokemonLocationSet)
                                  .title(getPockemon.name)
                                  .snippet(getPockemon.des + "power:  ${getPockemon.power}")
                                  .icon(BitmapDescriptorFactory.fromResource(getPockemon.image!!)))
                              if(location!!.distanceTo(getPockemon.location)<2f)
                              {
                                  getPockemon.isCatch=true
                                  listPockemons[i]=getPockemon

                                  playerPower+= getPockemon.power!!
                                  Toast.makeText(applicationContext,
                                      "You catch new pockemon your new pwoer is " + playerPower,
                                      Toast.LENGTH_LONG).show()

                              }

                          }


                        }
                    }
                    Thread.sleep(1000)
                }catch (ex:Exception){}

            }

        }
    }



    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION_CODE=123

    }

    var listPockemons = ArrayList<Pockemon>()
    var playerPower=0.0
    fun loadPockemon()
    {
        listPockemons.add(Pockemon(R.drawable.charmander,
            "Charmander", "Charmander living in japan", 55.0, 37.7789994893035, -122.401846647263))
        listPockemons.add(Pockemon(R.drawable.bulbasaur,
            "Bulbasaur", "Bulbasaur living in usa", 90.5, 37.7949568502667, -122.410494089127))
        listPockemons.add(Pockemon(R.drawable.squirtle,
            "Squirtle", "Squirtle living in iraq", 33.5, 37.7816621152613, -122.41225361824))
    }

}
