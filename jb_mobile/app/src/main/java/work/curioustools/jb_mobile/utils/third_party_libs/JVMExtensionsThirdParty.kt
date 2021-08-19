package work.curioustools.jb_mobile.utils.third_party_libs

import com.google.gson.GsonBuilder


fun <T> T.toJson(pretty:Boolean = true,serializeNulls:Boolean = true):String{
    return try {
        GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson(this)
    }
    catch (t:Throwable){
        t.printStackTrace()
        "{}"
    }
}