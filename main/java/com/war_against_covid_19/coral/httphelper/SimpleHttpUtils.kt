package com.war_against_covid_19.coral.httphelper

import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.net.URLEncoder
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

object SimpleHttpUtils {
    private const val TAG = "SimpleHttpUtils"
    private const val CONNECT_TIME_OUT = 10000
    private const val READ_TIME_OUT = 10000

    /**
     * GET请求
     */
    fun get(url: String, mapParam: Map<String, String>?): String {
        // 如果有参数的话就拼接参数到url后面
        val urlParam = if (mapParam == null) url else "${url}?${converMap2String(mapParam)}"
        Log.i(TAG, urlParam)
        var connection: HttpURLConnection? = null
        var reader: BufferedReader? = null

        val okHttpBuilder = OkHttpClient().newBuilder()
        val client=okHttpBuilder.addInterceptor(NetworkInterceptor()).build()
        val request= Request.Builder().url(urlParam).build()
        client.newCall(request).execute().use {
            if (!it.isSuccessful)throw IOException("Unexpected code $it")
            for ((name, value) in it.headers) {
                println("$name: $value")
            }
            Log.i(TAG,it.code.toString())

            return it.body!!.string()        }

    }

    /**
     * POST请求 - 参数为JSON格式
     */
    fun post(url: String, jsonParam: String?): String {
       /* val okHttpBuilder = OkHttpClient().newBuilder()
        val client=okHttpBuilder.addInterceptor(NetworkInterceptor()).build()
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val request= Request.Builder().url(url).post(jsonParam!!.toRequestBody(mediaType)).build()*/

        val connection = buildURLConnection(url, false)
        connection.setContentType()
        connection.connect() // 建立连接
        // 向服务端发送请求参数
        if (!jsonParam.isNullOrEmpty()) {
            connection.outputStream.write(jsonParam.toByteArray(Charsets.UTF_8))
            connection.outputStream.flush()
            connection.outputStream.close()
        }
        // 从服务端获取响应码，连接成功是200
        val code = connection.responseCode
        Log.i(TAG, code.toString())
        // 根据响应码获取不同输入流
        val inStream = if (code == 200)
            connection.inputStream
        else
            connection.errorStream
        // 输入流转换成字符串
        val result = inStream.bufferedReader().lineSequence().joinToString()
        Log.i(TAG, result)
        connection.disconnect() //断开连接
        return result/*
        client.newCall(request).execute().use {
            if (!it.isSuccessful)throw IOException("Unexpected code $it")
            for ((name, value) in it.headers) {
                println("$name: $value")
            }
            Log.i(TAG,it.code.toString())

            return it.body!!.string()        }*/
    }

    /**
     * POST请求 - 参数为表格格式
     */
    fun post(url: String, mapParam: Map<String, String>): String {
        val connection = buildURLConnection(url, false)
        connection.setContentType(1)
        connection.connect() // 建立连接

        connection.outputStream.write(converMap2String(mapParam).toByteArray())
        connection.outputStream.flush()
        connection.outputStream.close()
        // 从服务端获取响应码，连接成功是200
        val code = connection.responseCode
        Log.i(TAG, code.toString())
        // 根据响应码获取不同输入流
        val inStream = if (code == 200)
            connection.inputStream
        else
            connection.errorStream
        // 输入流转换成字符串
        val result = inStream.bufferedReader().lineSequence().joinToString()
        Log.i(TAG, result)
        connection.disconnect() //断开连接
        return result
    }
    fun post(url: String, file: File): String {//upload file
        try {
            //val url = "http://" + serverIp + "/upload"
           //val file = File("/sdcard/image.png")
            val fileBody = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("audio_data", "image.png", fileBody)
                .build()
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
            val httpBuilder = OkHttpClient.Builder()
            val okHttpClient = httpBuilder
                .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
                .build()
            val response = okHttpClient.newCall(request).execute()
            val responseStr = response.body?.string()
            return responseStr!!
        } catch (e: Exception) {
            Log.i("upload", "error:$e")
        }

        return ""
    }
    fun post(url: String, file:ByteArray): String {//upload file
        try {
            //val url = "http://" + serverIp + "/upload"
            //val file = File("/sdcard/image.png")
            //val fileBody = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("audio_data", "voice_input.wav", file.toRequestBody())
                .build()
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
            val httpBuilder = OkHttpClient.Builder()
            val okHttpClient = httpBuilder
                .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
                .build()
            val response = okHttpClient.newCall(request).execute()
            val responseStr = response.body?.string()
            return responseStr!!
        } catch (e: Exception) {
            Log.i("upload", "error:$e")
        }

        return ""
    }
    /**
     * 创建一个基于URLConnection类的对象用于
     * @param url 接口地址
     * @param isGet GET请求还是POST
     */
    private fun buildURLConnection(url: String, isGet: Boolean = true): HttpURLConnection {
        val url = URL(url)
        val connection = url.openConnection() as HttpURLConnection
        connection.let {
            it.requestMethod = if (isGet) "GET" else "POST"
            it.connectTimeout = CONNECT_TIME_OUT
            it.readTimeout = READ_TIME_OUT
            it.doInput = true
            it.doOutput = true
            it.useCaches = isGet // POST请求不能使用缓存
            it.instanceFollowRedirects = true // 是否允许HTTP的重定向
        }
        return connection
    }


    /**
     * 设置请求编码格式
     * @param 0用于JSON格式，1用于表单格式，3用于传输JAVA序列化对象
     */
    private fun URLConnection.setContentType(type: Int = 0) {
        val typeString = when (type) {
            1 -> "application/x-www-form-urlencoded"
            2 -> "application/x-java-serialized-object"
            else -> "application/json;charset=UTF-8"
        }
        this.setRequestProperty("Content-Type", typeString)
    }

    /**
     * 把map集合的参数拼接成字符串
     * @param isEncode 是否需要转换码
     */
    private fun converMap2String(mapParam: Map<String, String>, isEncode: Boolean = true): String {
        return mapParam.keys.joinToString(separator = "&") { key ->
            val value = if (isEncode) URLEncoder.encode(mapParam[key], "utf-8") else mapParam[key]
            "$key=$value"
        }
    }
}