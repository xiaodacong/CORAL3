package com.war_against_covid_19.coral

import android.content.Context
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.war_against_covid_19.coral.dbhelper.TrackingDBHelper
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class RiskLevelAndTrackingFragment : Fragment() {
    private val TAG = "Display"
    var trackingDBHelper: TrackingDBHelper by Delegates.notNull()
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        //5
        //udid!profile power consumption liangkai
       // val madison_height=LatLng(42.3574273,-83.0675309)
        val zoomlevel=16.0
        var hole:List<LatLng> =listOf()
        val holes=hole.toMutableList()
        val cursor=trackingDBHelper.getRecords()
        Log.i(TAG,"start to add markers")
        if(cursor!=null&&cursor.moveToFirst())

        {
            val current = LatLng(cursor.getFloat(2).toDouble(),cursor.getFloat(1).toDouble())
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current,zoomlevel.toFloat()))
        }
        if(cursor!=null&&cursor.moveToFirst()){

            do{
                Log.i(TAG, cursor.getFloat(2).toString()+ " "+cursor.getFloat(1).toString()+" "+cursor.getLong(0).toString())
                googleMap.addMarker(MarkerOptions().position(LatLng(cursor.getFloat(2).toDouble(),cursor.getFloat(1).toDouble())).title(
                SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(cursor.getLong(0))).icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
                holes.add(LatLng(cursor.getFloat(2).toDouble(),cursor.getFloat(1).toDouble()))
            }while(cursor.moveToNext())

        }
        //googleMap.addPolygon(PolygonOptions().fillColor(0x33000000).addHole(holes))
        //
        //googleMap.addMarker(MarkerOptions().position(current).title(Date().toString()))
        //googleMap.addMarker(MarkerOptions().position(madison_height).title(Date().toString()))
        //googleMap.addMarker(MarkerOptions().position(current).title(Date().toString()))

    }

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        trackingDBHelper = TrackingDBHelper(this.requireContext())
        return inflater.inflate(R.layout.fragment_risk_level_and_tracking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}