package work.curioustools.jb_mobile.commons

import androidx.annotation.Keep
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
import work.curioustools.third_party_network.interceptors.HeaderInterceptor
import work.curioustools.third_party_network.interceptors.LoggingInterceptor
import work.curioustools.third_party_network.utils.*
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkDI {

    @Singleton
    @Provides
    fun getGson(): Gson {
        return GsonUtils.getGsonObj(true)!! // todo get gson or error
    }

    @Singleton
    @Provides
    fun getMoshi(): Moshi {
        return MoshiUtils.getMoshiObj()!!//todo get moshi or error
    }

    @Singleton
    @Provides
    fun getGsonConvertor(gson: Gson): GsonConverterFactory {
        return GsonUtils.createGsonConvertor(gson)!!
    } // get gsonConvertor or error

    @Singleton
    @Provides
    fun getMoshiConvertor(moshi: Moshi): MoshiConverterFactory {
        return MoshiUtils.getMoshiConvertorOrError() // todo  accept moshi instance
    }

    @Singleton
    @Provides
    fun getScalerConvertor(): ScalarsConverterFactory {
        return ScalarConvertorUtils.getConvertorOrError()
    }


    @Singleton
    @Provides
    fun getLogInterceptor(): HttpLoggingInterceptor {
        return LoggingInterceptor.getInstance(HttpLoggingInterceptor.Level.BODY)//todo HttpLoggingInterceptorutils
    }

    @Singleton
    @Provides
    fun getOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return if (AppConfig.isDebug()) {//todo null by default
            OkHttpUtils.getClient(
                loggingInterceptor = loggingInterceptor
            )
        }
        else {
            OkHttpUtils.getClient(loggingInterceptor = null)
        }
    }

    @Singleton
    @Provides
    fun getTemporaryBaseUrl() = "https://www.google.com"

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
            externalOkHttpClient = okHttpClient,
            externalConvertors = mutableListOf(scalarConvertor, moshiConverter),//todo : by default empty, make special object for retrofit with moshi/scale default
            callAdapterFactories = listOf(),
        )
    }

    @Singleton
    @Provides
    @NetworkWithHeaders
    fun getHeaderInterceptor(): HeaderInterceptor //todo not needed here
    {
        return HeaderInterceptor(hashMapOf(HeaderInterceptor.HEADER_CONTENT_TYPE_PAIR))
    }


    @Singleton
    @Provides
    @NetworkWithHeaders
    fun getOkHttpClientWithHeaders(
        loggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: HeaderInterceptor
    ): OkHttpClient {
        return OkHttpUtils.getClient(
            loggingInterceptor = loggingInterceptor,
            otherInterceptors = listOf(headerInterceptor)//todo use headers directly
        )
    }

    @Singleton
    @Provides
    @NetworkWithHeaders
    fun getRetrofitClientWithHeaders(
        baseUrl: String,
        okHttpClientWithDummyHeaders: OkHttpClient,
        scalarConvertor: ScalarsConverterFactory,
        moshiConverter: MoshiConverterFactory,
    ): Retrofit {
        return RetrofitUtils.getRetrofit(
            baseUrl = baseUrl,
            externalOkHttpClient = okHttpClientWithDummyHeaders,
            externalConvertors = mutableListOf(scalarConvertor, moshiConverter),
        )
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    @Keep
    annotation class NetworkWithHeaders

}