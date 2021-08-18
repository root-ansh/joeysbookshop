package work.curioustools.jb_mobile.utils.third_party_libs.network_utils.interceptors

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import okhttp3.Interceptor
import work.curioustools.jb_mobile.utils.third_party_libs.network_utils.NetworkUtils
import java.io.IOException

class InternetAvailabilityInterceptor constructor(private val ctx: Context) : Interceptor {
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {

        if (!NetworkUtils.isNetworkAvailable(ctx)) {
            throw IOException("INTERNET NOT AVAILABLE")
        } else {
            return chain.proceed(chain.request())
        }

    }
}