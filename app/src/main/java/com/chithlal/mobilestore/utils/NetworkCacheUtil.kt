package com.chithlal.mobilestore.utils

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.chithlal.mobilestore.model.Features
import com.google.gson.Gson
import java.io.File

class NetworkCacheUtil(val context: Context) {

    val cacheFileName = "nwcache"
    private val FILE_NAME = "filename"

    /***
     * @param activity activity instance to create shared preferance objects
     * @param feature object which need to be saved
     *
     * write response as a string to cache file
     * returns Unit
     * */
    fun writeCacheFile(activity: Activity, feature: Features) {
        val gson = Gson()
        val dataString = gson.toJson(feature) // convert object to json string
        val file = File.createTempFile(cacheFileName, "", context.cacheDir) //create tempfile
        val lastCacheFile =
            File(context.cacheDir, getTempFileName(activity)) // delete previous cache
        lastCacheFile.delete()
        storeCacheFilename(activity, file.name) // store new response
        file.writeText(dataString)

    }


    /***
     * Read cache file from cache dir
     *
     * @param activity to access shared prefs
     * @return Features object
     * */
    fun readCacheFile(activity: Activity): Features? {
        var outString = ""
        var features: Features? = null
        try {
            val tempCacheFileName = getTempFileName(activity)
            val cacheFile = File(context.cacheDir, tempCacheFileName)
            if (cacheFile != null) {
                outString = cacheFile.readText()
            }
        } catch (e: Exception) {
            Log.d(TAG, "readCacheFile: No cache available")
        }


        if (outString != "") {
            features = Gson().fromJson(outString, Features::class.java)
        }
        return features
    }

    /***
     * Store cache file name for future reference
     * @param activity to access shared prefs
     * @param fileName to be stored
     * */
    private fun storeCacheFilename(activity: Activity, fileName: String) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(FILE_NAME, fileName)
            apply()
        }
    }

    /***
     * get last written file name from shared prefs
     * @param activity to create shared prefs objects
     * @return string (file name)*/

    private fun getTempFileName(activity: Activity): String {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getString(FILE_NAME, "")!!

    }
}