package com.war_against_covid_19.coral

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.war_against_covid_19.coral.httphelper.SimpleHttpUtils
import kotlinx.android.synthetic.main.fragment_local_news.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit


//user input or current location
/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class LocalNewsFragment : Fragment() {
    private lateinit var client: OkHttpClient
    private lateinit var builder: OkHttpClient.Builder

    val JSON: MediaType = "application/json".toMediaType()
    private var newstext: String ="fail"
    data class A1(
        val l1: String? ,
        val l2: Int?
    )

    /**
     * Get请求
     */
    private fun onGetRequest()= GlobalScope.launch {//longi lati
        // val dlg = indeterminateProgressDialog("loading...")
        // 接名地址
        val url ="http://141.217.48.207:8080/edge_api/v1.5/device/request_news"// "https://www.yongtao.codes:8080/edge_api/v1.0/device/request_news"//not need to certificate client
        //val url="https://www.yongtao.codes:8080/edge_api/v1.0/device/request_news"
        // 接口参数

        val locationData = mapOf(
           // "format" to "1",
            "latitude" to "42.357440",
            "longitude" to "-83.065300"
           // "cityname" to "detroit"//,
         //   "key" to "3bc829216bb4ede1e846fe91b3df5543"
        )
        val mapParam=mapOf(
        //"loc_data" to "locationData.toString()",
       // "mode" to "geom"
        "fips" to "26"
        )
        // 挂起当前的协程并运行自定义的get方法，方法返回后再恢复协程
        val result = withContext(Dispatchers.IO) {
            SimpleHttpUtils.get(url, mapParam) // 发起GET请求
            //HttpHelper.get()
        }

        withContext(Dispatchers.Main){
            //val process_result=result
            val process_result=result.replace("[","").replace("]","").replace(",","").replace("\n\n\n","\n")
            localnews_text.text=process_result
            val news=""
            for (i in result.indices){
                news.plus(result[i].toString()+"\n")
            }
            //localnews_text.text=news
            //val result_json:A1=Gson().fromJson(result)
           // localnews_text.text=
        }
      //  newstext = result

        //  dlg.dismiss()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_local_news, container, false)
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        builder = OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .writeTimeout(10000, TimeUnit.MILLISECONDS)

        client = builder
            .build()
       // loginApi()
        try {
            onGetRequest()
        }
        catch (e:Exception )
        {

        }
        //localnews_text.text=newstext
        view.findViewById<Button>(R.id.button_second).setOnClickListener {
            findNavController().navigate(R.id.action_localNewsFragment_to_welcomeFragment)
        }
    }
}