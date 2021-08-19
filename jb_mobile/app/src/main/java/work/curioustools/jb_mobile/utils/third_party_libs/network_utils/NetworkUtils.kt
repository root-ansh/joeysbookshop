@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package work.curioustools.jb_mobile.utils.third_party_libs.network_utils


import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresPermission
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*
import kotlin.collections.HashMap

//==================================================================================================
/*
REQUIREMENTS:
- "com.squareup.retrofit2:retrofit:2.9.0")
- "com.squareup.retrofit2:converter-gson:2.9.0")
- "com.squareup.retrofit2:converter-moshi:2.9.0")
- "com.squareup.retrofit2:converter-scalars:2.9.0")
- "com.squareup.okhttp3:okhttp:4.9.1")
- "com.squareup.okhttp3:logging-interceptor:4.9.1")
- "org.jsoup:jsoup:1.13.1")
*/
//<EXTENSIONS> =====================================================================================

//</EXTENSIONS> ====================================================================================

//</HELPER_CLASSES> ================================================================================

//</DI> ============================================================================================

//<UTILS> ==========================================================================================
class NetworkUtils {
    companion object {
        private val di = NetworkDIManual()

        fun createRetrofitInstance(
            baseUrl: String,
            useMoshiConvertor: Boolean = true,
            useScalerConverotr: Boolean = true,
            headers: HashMap<String, String>? = null
        ): Retrofit {

            val okHttpClient = di.createHttpClient(headers)
            val scalerConvertor = di.getScalerConvertor()
            val gsonConvertor = di.createGsonConvertor()
            val moshiConverter = di.createMoshiConvertor()
            return Retrofit.Builder().run {
                baseUrl(baseUrl)
                client(okHttpClient)
                if (useScalerConverotr && scalerConvertor != null) {
                    addConverterFactory(scalerConvertor)
                }
                val convertor = if (useMoshiConvertor) moshiConverter else gsonConvertor
                if (convertor != null) addConverterFactory(convertor)
                build()
            }

        }


        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        @SuppressWarnings("deprecation")
        fun isNetworkAvailable(ctx: Context): Boolean {
            val cm = ctx.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                val capabilities = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }

            } else {
                cm.activeNetworkInfo?.isConnectedOrConnecting == true
            }
        }


    }
}
//</UTILS> =========================================================================================

//<Example>=================================================================//
/*
    interface PokeApi{

        @GET(ENDPOINT_POKEMON_BY_ID)
        fun getPokemon(@Path(ID)id:Int): Call<String>

        @GET(POKEMON)
        fun getNextInfo(@Query(LIMIT) limit:Int): Call<String>


        companion object{
            //paths and query variables
            const val ID ="id"
            const val LIMIT = "limit"
            const val POKEMON = "pokemon"

            const val BASE = "https://pokeapi.co/api/v2/"


            const val ENDPOINT_POKEMON_BY_ID ="$POKEMON/{$ID}"

            fun main(){

                val api = PokeApi::class.java.getApi(PokeApi.BASE)
                val api2 = NetworkUtils.getApiObject(PokeApi::class.java,PokeApi.BASE)
                println(api.getNextInfo(1).execute().body())

            }
        }

    }
*/
//</Example>================================================================//



//todo document:
// - why scaler convertors are used
// - why miscallanous things are used