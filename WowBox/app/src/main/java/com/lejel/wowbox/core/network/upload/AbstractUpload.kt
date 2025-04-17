package com.lejel.wowbox.core.network.upload

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.os.Handler
import android.os.Looper
import android.webkit.MimeTypeMap
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import com.lejel.wowbox.Const
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.core.util.PplusCommonUtil.Companion.getDeviceID
import com.lejel.wowbox.core.util.imageresizer.ImageResizer
import com.lejel.wowbox.core.util.imageresizer.operations.ImageRotation
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.internal.closeQuietly
import okio.Buffer
import okio.BufferedSink
import okio.Source
import okio.source
import java.io.File
import java.io.FileFilter
import java.io.IOException
import java.lang.reflect.Type
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * Created by j2n on 2016. 9. 30..
 */
abstract class AbstractUpload<T : Any?> protected constructor(pplusCallback: PplusUploadListener<T>) {
    val LOG_TAG = javaClass.simpleName
    private val okHttpClient: OkHttpClient
    private val callMap: MutableMap<String, Call>
    private val callQueue: LinkedBlockingQueue<Call>
    private val threadPool: ThreadPoolExecutor
    private val pplusCallback: PplusUploadListener<T>

    /* 업로드 주소 */
    abstract fun getUploadUrl(): String

    /* 멀티 쓰레드 사용여부*/
    abstract fun isMultiThreadEnable(): Boolean
    abstract fun getResultType(): TypeToken<NewResultResponse<T>>

    init {

        //        this.okHttpClient
        val clientbuilder = OkHttpClient.Builder()
        clientbuilder.connectTimeout(30, TimeUnit.SECONDS)
        clientbuilder.writeTimeout(180, TimeUnit.SECONDS)
        clientbuilder.readTimeout(30, TimeUnit.SECONDS)
        clientbuilder.addNetworkInterceptor(Interceptor { chain ->
            val original = chain.request()
            val builder = original.newBuilder()
            builder.method(original.method, original.body)
            if (LoginInfoManager.getInstance().isMember()) {
                LoginInfoManager.getInstance().member!!.token?.let { builder.addHeader("token", it) }
            }
            builder.addHeader("device", getDeviceID())
            builder.addHeader("api_key", "Rx4FTCJDi3MwyquI")
            chain.proceed(builder.build())
        })
        clientbuilder.retryOnConnectionFailure(true)
        okHttpClient = clientbuilder.build()
        this.pplusCallback = pplusCallback
        callMap = HashMap()
        callQueue = LinkedBlockingQueue()
        var maxThreadCount = 1
        if (isMultiThreadEnable()) {
            maxThreadCount = getNumCores()
        }
        threadPool = ThreadPoolExecutor(maxThreadCount, maxThreadCount, 60, TimeUnit.SECONDS, LinkedBlockingQueue())
    }

    fun request(tag: String, params: HashMap<String, Any>) {
        run(UploadRunnable(tag, params))
    }

    /**
     * 요청중인 콜에대해 취소합니다.
     */
    fun stop(pplusUploadCancelListener: PplusUploadCancelListener) {
        val callIterator: Iterator<String> = callMap.keys.iterator()
        val stringSet: MutableSet<String> = HashSet()
        while (callIterator.hasNext()) {
            val tag = callIterator.next()
            if (callMap.containsKey(tag)) {
                callMap[tag]!!.cancel()
                callMap.remove(tag)
                stringSet.add(tag)
            }
        }
        pplusUploadCancelListener.onCancel(stringSet)
    }

    private fun run(runnable: Runnable) {
        threadPool.execute(runnable)
    }

    private inner class UploadRunnable(private val tag: String, private val params: HashMap<String, Any>) : Runnable {
        override fun run() {


            //            synchronized(callMap) {
            try {
                synchronized(params) {
                    params["file"] = buildFile(params["file"] as File)
                }
                val call = okHttpClient.newCall(getRequest(params))
                callMap[tag] = call
                val response = call.execute()
                if (Const.DEBUG_MODE) {
                    try {
                        LogUtil.e(LOG_TAG, "Successful = {} ", response.isSuccessful)
                        LogUtil.e(LOG_TAG, "request Url = {} ", response.request.url.toString())
                        val copy = response.request.body
                        if (copy != null) {
                            val buffer = Buffer()
                            copy.writeTo(buffer)
//                            LogUtil.e(LOG_TAG, "request body = {} ", buffer.readUtf8())
                        }
                    } catch (e: IOException) {
                        LogUtil.e(LOG_TAG, e.toString())
                    }
                }
                if (!response.isSuccessful) {
                    val resultResponse: NewResultResponse<*> = NewResultResponse<Any?>()
                    resultResponse.code = 500
                    onFailure(tag, resultResponse)
                } else {
                    val responseResult = response.body!!.string()
                    val gsonBuilder = GsonBuilder()
                    val gson = gsonBuilder.serializeNulls().disableHtmlEscaping().create()
                    val collectionType: Type = getResultType().type
                    val resultResponse = gson.fromJson<NewResultResponse<T>>(responseResult, collectionType)
                    if (resultResponse.code == 200) {
                        onResult(tag, resultResponse)
                    } else {
                        onFailure(tag, resultResponse)
                    }
                }
            } catch (e: IOException) {
                LogUtil.e(LOG_TAG, e.toString())
                val resultResponse: NewResultResponse<*> = NewResultResponse<Any?>()
                resultResponse.code = 500
                onFailure(tag, resultResponse)
            } finally {
                if (callMap.containsKey(tag)) callMap.remove(tag)
            }
        }

        // 성공 처리
        fun onResult(tag: String?, resultResponse: NewResultResponse<T>?) {
            Handler(Looper.getMainLooper()).post { pplusCallback.onResult(tag, resultResponse) }
        }

        // 실패 처리
        fun onFailure(tag: String?, resultResponse: NewResultResponse<*>?) {
            Handler(Looper.getMainLooper()).post { pplusCallback.onFailure(tag, resultResponse) }
        }
    }

    fun buildFile(file: File): File {
        val path = file.path
        val originFile = File(path)
        var imageRotation: ImageRotation? = null
        try {
            val exif = ExifInterface(path)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> imageRotation = ImageRotation.CW_90

                ExifInterface.ORIENTATION_ROTATE_180 -> imageRotation = ImageRotation.CW_180

                ExifInterface.ORIENTATION_ROTATE_270 -> imageRotation = ImageRotation.CW_270
            }
        } catch (e: IOException) {
            LogUtil.e(LOG_TAG, e.toString())
        }
        val tempFile = File(PplusCommonUtil.getAlbumDir(), originFile.name)
        if (!tempFile.exists()) {
            try {
                tempFile.createNewFile()
            } catch (e: IOException) {
                LogUtil.e(LOG_TAG, e.toString())
            }
        }
        var bitmap: Bitmap? = null //        if (isResize) { //            try {
        //                bitmap = if (imageRotation != null) {
        //                    when (imageRotation) {
        //                        ImageRotation.CW_90, ImageRotation.CW_180 -> ImageResizer.resize(originFile, 1280, 720, ResizeMode.FIT_TO_HEIGHT)
        //                        else -> ImageResizer.resize(originFile, 720, 1280, ResizeMode.FIT_TO_WIDTH)
        //                    }
        //                } else {
        //                    ImageResizer.resize(originFile, 720, 1280, ResizeMode.FIT_TO_WIDTH)
        //                }
        //                if (bitmap == null) {
        //                    LogUtil.e(LOG_TAG, "bitmap is null")
        //                }
        //                ImageResizer.saveToFile(bitmap, tempFile)
        //            } catch (error: OutOfMemoryError) {
        //                LogUtil.e(LOG_TAG, "error = {}", error)
        //            } finally {
        //                bitmap?.recycle()
        //            }
        //        }
        if (imageRotation != null) {
            try {
                bitmap = ImageResizer.rotate(tempFile, imageRotation)
                ImageResizer.saveToFile(bitmap, tempFile)
            } catch (error: OutOfMemoryError) {
                LogUtil.e(LOG_TAG, "error = {}", error)
            } finally {
                bitmap?.recycle()
            }
        }
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(tempFile.path, options)
        bitmap?.recycle()

        // 최종 파일 변경
        return tempFile
    }

    private fun getRequest(params: Map<String, Any>): Request {
        var requestBody: RequestBody? = null
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val keys = params.keys.iterator()
        while (keys.hasNext()) {
            val key = keys.next()
            val value = params[key]
            if (value != null) {
                if (value is File) {

                    val fileBody: RequestBody = object : RequestBody() {
                        override fun contentType(): MediaType? {
                            return getMimeType(value)?.toMediaTypeOrNull()
                        }

                        override fun contentLength(): Long {
                            return value.length()
                        }

                        @Throws(IOException::class)
                        override fun writeTo(sink: BufferedSink) {
                            var source: Source? = null
                            try {
                                source = value.source()
                                sink.writeAll(source)
                                LogUtil.e(LOG_TAG, "file upload... source = {}", source)
                            } finally {
                                source!!.closeQuietly()
                            }
                        }
                    }
                    builder.addFormDataPart(key, value.name, fileBody)
                } else if (value is String) {
                    builder.addFormDataPart(key, params[key] as String)
                } else if (value is Enum<*>) {
                    builder.addFormDataPart(key, (params[key] as Enum<*>?)!!.name)
                } else if (value is Long || value is Int || value is Float) {
                    builder.addFormDataPart(key, params[key].toString())
                }
            }
        }
        requestBody = builder.build()
        return Request.Builder().url(getUploadUrl()).post(requestBody).build()
    }

    fun getMimeType(file: File): String? {
        return getMimeType(file.path)
    }

    fun getMimeType(url: String?): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        if (StringUtils.isEmpty(type)) {
            type = "*/*"
        }
        return type
    }


    //Check if filename is "cpu", followed by a single digit number


    //Get directory containing CPU info

    //Filter to only list the devices we care about

    //Return the number of cores (virtual CPU devices)
    //Private Class to display only CPU devices in the directory listing
    private fun getNumCores(): Int {

        //Private Class to display only CPU devices in the directory listing
        class CpuFilter : FileFilter {
            override fun accept(pathname: File): Boolean {

                //Check if filename is "cpu", followed by a single digit number
                return Pattern.matches("cpu[0-9]", pathname.name)
            }
        } //Get directory containing CPU info

        val dir = File("/sys/devices/system/cpu/")

        //Filter to only list the devices we care about
        val files = dir.listFiles(CpuFilter())

        //Return the number of cores (virtual CPU devices)
        return files?.size ?: 1
    }
}