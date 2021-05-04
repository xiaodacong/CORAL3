package com.war_against_covid_19.coral

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.war_against_covid_19.coral.dbhelper.TrackingDBHelper
import com.war_against_covid_19.coral.httphelper.SimpleHttpUtils
import kotlinx.android.synthetic.main.fragment_upload_user_location.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
//import okhttp3.internal.http2.Settings
import java.util.*
import kotlin.properties.Delegates

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UploadUserLocationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UploadUserLocationFragment : Fragment() {
    var trackingDBHelper: TrackingDBHelper by Delegates.notNull()
    private val TAG = "Upload"
    object TrackingJson {
        private val TAG = "Convert"
        fun Convert(cursor: Cursor):String{
            //val JsonString="{}"
            val gps= mutableListOf<LocationRecord>()
            Log.i(TAG, "start to convert")
            if(cursor!=null&&cursor.moveToFirst())
            {
                do{
                    Log.i(
                        TAG,
                        cursor.getFloat(2).toString() + " " + cursor.getFloat(1)
                            .toString() + " " + cursor.getLong(
                            0
                        ).toString()
                    )
                    gps.add(
                        LocationRecord(
                            cursor.getFloat(2),
                            cursor.getFloat(1),
                            cursor.getLong(0).toString()
                        )
                    )

                }while(cursor.moveToNext())

            }
            val uploadStructure=UploadStructure(gps, Date().time.toString())

            val JsonString=formatJson(Gson().toJson(uploadStructure))
            Log.i(TAG, "convert result: $JsonString")
            return(JsonString)
        }
        private fun formatJson(content: String): String {
            val gson = GsonBuilder().setPrettyPrinting().create()
            val jsonParser = JsonParser()
            val jsonElement = jsonParser.parse(content)
            return gson.toJson(jsonElement)
        }


    }

    fun getDeviceUniqueId(ctx: Context): String? {
        return Settings.Secure.getString(
            ctx.getContentResolver(),
            Settings.Secure.ANDROID_ID
        )
    }
    data class LocationRecord(val latitude: Float, val longitude: Float, val datetime: String)
    data class UploadStructure(var gps: List<LocationRecord>, val datetime: String)
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private fun onPostRequest()= GlobalScope.launch {//longi lati
        // val dlg = indeterminateProgressDialog("loading...")
        // 接名地址
        val url =
            "http://141.217.48.207:8080/edge_api/v1.0/device/update_info"// "https://www.yongtao.codes:8080/edge_api/v1.0/device/request_news"//not need to certificate client
        // 接口参数
        //val datetime= Date().time.toString()
        val cursor=trackingDBHelper.getRecords()
        val jsonParam =TrackingJson.Convert(cursor)//+getDeviceUniqueId(this)
        Log.i(TAG, "ready to upload: $jsonParam")
        //    "{\"gps\": [{\"latitude\":42.332940, \"longitude\": -83.047840, \"datetime\":\"20200819\"}],\"datetime\":\"20200819\"}"

        val result = withContext(Dispatchers.IO) {
            SimpleHttpUtils.post(url, jsonParam) // 发起GET请求
            //HttpHelper.get()
        }
        withContext(Dispatchers.Main) {
            upload_user_location_textview.text = result

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        trackingDBHelper = TrackingDBHelper(this.requireContext())
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload_user_location, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        onPostRequest()



    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UploadUserLocationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UploadUserLocationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}