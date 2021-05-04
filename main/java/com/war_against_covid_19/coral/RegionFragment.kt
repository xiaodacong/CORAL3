package com.war_against_covid_19.coral

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.war_against_covid_19.coral.httphelper.SimpleHttpUtils
import kotlinx.android.synthetic.main.fragment_local_news.*
import kotlinx.android.synthetic.main.fragment_region.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private fun onGetRequest()= GlobalScope.launch {//longi lati
        // val dlg = indeterminateProgressDialog("loading...")
        // 接名地址
        val url ="http://141.217.48.207:8080/edge_api/v1.0/device/request_region_rl"// "https://www.yongtao.codes:8080/edge_api/v1.0/device/request_news"//not need to certificate client
        // 接口参数
        val mapParam = mapOf(
            // "format" to "1",
            "latitude" to "42.332940",
            "longitude" to "-83.047840"
            // "cityname" to "detroit"//,
            //   "key" to "3bc829216bb4ede1e846fe91b3df5543"
        )
        // 挂起当前的协程并运行自定义的get方法，方法返回后再恢复协程
        val result = withContext(Dispatchers.IO) {
            SimpleHttpUtils.get(url, mapParam) // 发起GET请求
            //HttpHelper.get()
        }
        withContext(Dispatchers.Main){
            location_rl_textview.text=result
            // val result_json:A1=Gson().fromJson(result)
            // localnews_text.text=
        }
        //  newstext = result

        //  dlg.dismiss()
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_region, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        onGetRequest()

        //localnews_text.text=newstext

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}