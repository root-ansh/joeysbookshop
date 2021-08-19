package work.curioustools.jb_mobile.utils.third_party_libs

import com.google.gson.Gson


@RequiresDependency(""" implementation "com.squareup.retrofit2:converter-gson:2.9.0""")
fun <T> String.toPojoModel(clazz:Class<T>):T?{
    return try {
        Gson().fromJson(this,clazz)
    }
    catch (t:Throwable){
        t.printStackTrace()
        null
    }
}
