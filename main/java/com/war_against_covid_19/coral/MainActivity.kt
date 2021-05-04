package com.war_against_covid_19.coral

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.war_against_covid_19.coral.dbhelper.TrackingDBHelper
import com.war_against_covid_19.coral.httphelper.SimpleHttpUtils
import com.war_against_covid_19.coral.utils.requestPermissions
import kotlinx.android.synthetic.main.fragment_local_news.*
import kotlinx.coroutines.Runnable
import java.net.URL
import kotlin.properties.Delegates

//import kotlinx.coroutines.
//import kotlinx.android.synthetic.main.location_activity.*
//import com.google.android.gms.location.*
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
    private lateinit var locationManager: LocationManager
   // private lateinit var fusedLocationClient: FusedLocationProviderClient
   // private lateinit var locationRequest: LocationRequest
   // private lateinit var locationCallback: LocationCallback
    var trackingDBHelper: TrackingDBHelper by Delegates.notNull()
    val locationListener = object : LocationListener {
       override fun onProviderDisabled(provider: String) {
           // toast("关闭了GPS")
            //textView.text = "关闭了GPS"
        }

       override fun onProviderEnabled(provider: String) {
           // toast("打开了GPS")
           // showLocation(textView, locationManager)
        }

        override fun onLocationChanged(location: Location) {
           // toast("变化了")
           // showLocation(textView, locationManager)

            Log.i(TAG,"New location recording")
            val url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                    "${location.latitude},${location.longitude}&language=zh-CN&sensor=false"
            // val value: Any = Uri uri = Uri . parse （ url)


            //addressTextView.text = URL(url).readText()
            trackingDBHelper.record(" ",location.longitude.toFloat(),location.latitude.toFloat())
            //trackingDBHelper.record(SimpleHttpUtils.get(url,null),location.longitude.toFloat(),location.latitude.toFloat())
            Log.i(TAG, location.longitude.toString()+" "+location.latitude.toString())
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        //sendRequestWithHttpURLConnection()
        trackingDBHelper = TrackingDBHelper(this)
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        requestPermissions(locationPermission,requestCode=1)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.i(TAG,"Not authorized to access location info")
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        else {
            locationManager.requestLocationUpdates(//.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                2000,
                10.0f, locationListener
            )
            Log.i(TAG,"Add listener")
        }
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
       // refresh_localnews_button.setOnClickListener {
        //    onGetRequest()
       // }


    }

  /*  private fun requestLocationService() {
        if (LocationServiceEnable()) requestLocationUpdate()
    }
    @SuppressLint("MissingPermission")
    private fun requestLocationUpdate() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()
        )
    }

   */
    /*fun showResponse(responseData: String?) {
        runOnUiThread(object : Runnable {
            override fun run() {
                localnews_text?.text = responseData
            }
        })
    }*/
}