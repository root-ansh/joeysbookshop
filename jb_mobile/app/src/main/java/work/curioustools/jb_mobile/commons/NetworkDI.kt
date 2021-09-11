package work.curioustools.jb_mobile.commons

import com.google.gson.Gson
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import work.curioustools.third_party_network.interceptors.OkHttpLoggingInterceptorUtils
import work.curioustools.third_party_network.utils.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkDI {

    @Singleton
    @Provides
    fun getGson(): Gson {
        return GsonUtils.getGsonOrError(true)
    }

    @Singleton
    @Provides
    fun getMoshi(): Moshi {
        return MoshiUtils.getMoshiOrError()
    }

    @Singleton
    @Provides
    fun getGsonConvertor(gson: Gson): GsonConverterFactory {
        return GsonUtils.getGsonConvertorOrError(gson)
    } // get gsonConvertor or error

    @Singleton
    @Provides
    fun getMoshiConvertor(moshi: Moshi): MoshiConverterFactory {
        return MoshiUtils.getMoshiConvertorOrError(moshi)
    }

    @Singleton
    @Provides
    fun getScalerConvertor(): ScalarsConverterFactory {
        return ScalarConvertorUtils.getConvertorOrError()
    }


    @Singleton
    @Provides
    fun getLogInterceptor(): HttpLoggingInterceptor {
        return OkHttpLoggingInterceptorUtils.getInterceptor(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun getOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return if (AppConfig.isDebug()) {
            OkHttpUtils.getClient(
                loggingInterceptor = loggingInterceptor
            )
           // OkHttpUtils.getDebugClient() //todo custom options for debugging
        }
        else {
            val oneMinute  = Pair(1L, TimeUnit.MINUTES)
            OkHttpUtils.getClient(
                connectTimeout = oneMinute,
                writeTimeout = oneMinute,
                readTimeout = oneMinute,
                retryOnConnectionFailure = true,
                headerInterceptor = null,
                loggingInterceptor = null,
                internetCheckInterceptor = null,
                otherInterceptors = listOf(),
                stetho = null,
                otherNetworkInterceptors = listOf(),
                socketFactory = null

            )
        }
    }

    @Singleton
    @Provides
    fun getTemporaryBaseUrl():String = "https://www.google.com"

    @Singleton
    @Provides
    fun getRetrofitClient(
        baseUrl: String,
        scalarConvertor: ScalarsConverterFactory,
        moshiConverter: MoshiConverterFactory,
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return RetrofitUtils.getRetrofit(
            baseUrl = baseUrl,
            okHttpClient = okHttpClient,
            externalConvertors = mutableListOf(scalarConvertor, moshiConverter),//todo : support moshi 1.12
            callAdapterFactories = listOf(),
        )
    }

}