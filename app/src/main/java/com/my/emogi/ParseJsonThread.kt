package com.my.emogi

import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.Log
import com.google.gson.Gson
import com.my.emogi.model.Assets
import org.json.JSONException
import org.json.JSONObject
import java.io.InputStreamReader

class ParseJsonThread(var context: Context, var handler: Handler): Thread()
{
    val TAG=ParseJsonThread::class.java.simpleName

    override fun run() {
        var json = InputStreamReader(context.getAssets().open(Constants.FILE_NAME)).readText()
        var map = parseContent(JSONObject(json))

        var msg=Message()
        msg.what=Constants.MSG_PARSE_JSON
        msg.data.putSerializable(Constants.MSG_PARSE_JSON_DATA,map)
        handler.sendMessage(msg)

    }
    inline fun <reified T : Any> Gson.fromJson(json: String): T {
        return Gson().fromJson(json, T::class.java)
    }
    private fun parseContent(mainObject: JSONObject) :  HashMap<String, ArrayList<Assets>>{
        val tagMap : HashMap<String, ArrayList<Assets>> = HashMap()
        try {
            val header = mainObject.getJSONObject(Constants.CONTENTS)
            val keys = header.keys()

            while (keys.hasNext()) {
                val currKey = keys.next() as String

                if (header.get(currKey) is JSONObject) {
                    val subHeaderObject = header.getJSONObject(currKey)

                    val assetsArray = subHeaderObject.getJSONArray(Constants.ASSETS)
                    val tagsArray = subHeaderObject.getJSONArray(Constants.TAGS)

                    var fullAsset : Assets?= null

                    for (i in 0 until assetsArray.length()) {
                        var asset: Assets = Gson().fromJson(assetsArray.get(i).toString() )
                        if (asset.isThumb())
                            fullAsset=asset
                    }

                    if (fullAsset!=null) {
                        for (i in 0 until tagsArray.length()) {
                            val tag = tagsArray.getString(i)
                            if (!tagMap.containsKey(tag))
                                tagMap.put(tag, ArrayList<Assets>())
                            tagMap.get(tag)?.add(fullAsset)
                        }
                    }

                }
            }

        } catch (e: JSONException) {
            Log.e(TAG, "Parse error "+e.message)
            e.printStackTrace()
        }

        return tagMap

    }
}