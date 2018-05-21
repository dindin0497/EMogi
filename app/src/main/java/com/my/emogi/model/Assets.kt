package com.my.emogi.model

class Assets
{
    var asset_id: String ? =null
    var url: String ? =null
    var file_extension: String ? =null
    var size: String ? =null

    fun isThumb() : Boolean
    {
        return size?.equals("thumb",true)?:false
    }
}