package com.chithlal.mobilestore.utils

import android.app.Activity
import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.chithlal.mobilestore.model.Features
import com.google.gson.Gson
import java.io.File
import java.io.FileInputStream

class NetworkCacheUtil(val context: Context) {

    val cacheFileName = "nwcache"
    private val FILE_NAME = "filename"

    fun writeCacheFile(activity: Activity,feature: Features){
        val gson = Gson()
        val dataString = gson.toJson(feature)
        val file = File.createTempFile(cacheFileName,"", context.cacheDir)
        val lastCacheFile = File(context.cacheDir, getTempFileName(activity)) // delete previous cache
        lastCacheFile.delete()
        storeCacheFilename(activity,file.name)
        file.writeText(dataString)

    }

    fun readCacheFile(activity: Activity): Features?{
        var outString = ""
        var features: Features? = null
        try {
            val tempCacheFileName = getTempFileName(activity)
            val cacheFile = File(context.cacheDir, tempCacheFileName)
            if (cacheFile != null){
                outString = cacheFile.readText()
            }
        }
        catch (e: Exception){
            Log.d(TAG, "readCacheFile: No cache available")
        }


     if (outString != ""){
          features = Gson().fromJson(outString,Features::class.java)
     }
        return features
    }

    private fun storeCacheFilename(activity: Activity,fileName: String){
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(FILE_NAME, fileName)
            apply()
        }

    }
    private fun getTempFileName(activity: Activity): String{
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getString(FILE_NAME, "")!!

    }
}