package com.war_against_covid_19.coral

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.war_against_covid_19.coral.httphelper.SimpleHttpUtils
import com.war_against_covid_19.coral.recordhelper.RecordHelper
import com.war_against_covid_19.coral.utils.requestPermissions
import kotlinx.android.synthetic.main.fragment_upload_user_location.*
import kotlinx.android.synthetic.main.fragment_upload_voice.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import java.io.File
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UploadVoiceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UploadVoiceFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val voicePermission =arrayOf(Manifest.permission.RECORD_AUDIO)

    private fun onPostRequest(data:ByteArray)= GlobalScope.launch {//longi lati
        // val dlg = indeterminateProgressDialog("loading...")
        // 接名地址
        val url =
            "http://141.217.48.207:8080/edge_api/v1.0/device/request_audio"// "https://www.yongtao.codes:8080/edge_api/v1.0/device/request_news"//not need to certificate client
        // 接口参数
      //  val audio_file=R.raw.test_audio//"./res/raw/test_audio.wav")

        val result = withContext(Dispatchers.IO) {
            SimpleHttpUtils.post(url, data) // 发起GET请求
            //HttpHelper.get()
        }
        withContext(Dispatchers.Main) {
            upload_voice_textview.text = result

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload_voice, container, false)
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions(voicePermission,1)
        val audioRecord=RecordHelper.instance

        view.findViewById<Button>(R.id.record_button).setOnTouchListener{view, motionEvent ->
           when(motionEvent.action){
               MotionEvent.ACTION_DOWN -> {
                   audioRecord.startRecord()
                    //audioRecord.stopRecord()

               }
               MotionEvent.ACTION_UP->{
                   audioRecord.stopRecord()
                   onPostRequest(audioRecord.returnData())
               }
               else ->{

               }
           }
            true


           //if(motionEvent.action==MotionEvent.ACTION_DOWN) audioRecord.startRecord()




        }





    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UploadVoiceFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UploadVoiceFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}