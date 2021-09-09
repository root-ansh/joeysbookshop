package work.curioustools.jb_mobile.utils.third_party_libs.network_utils

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
import work.curioustools.third_party_network.interceptors.LoggingInterceptor
import work.curioustools.third_party_network.utils.*
import javax.inject.Qualifier
import javax.inject.Singleton


@Module @InstallIn(SingletonComponent::class)
class NetworkDI {

    @Singleton @Provides
    fun getGson(): Gson = GsonUtils.getGsonObj(true)!!

    @Singleton @Provides
    fun getMoshi(): Moshi = MoshiUtils.getMoshiObj()!!

    @Singleton @Provides
    fun getGsonConvertor(gson: Gson): GsonConverterFactory = GsonUtils.createGsonConvertor(gson)!!

    @Singleton @Provides
    fun getMoshiConvertor(moshi: Moshi): MoshiConverterFactory = MoshiConverterFactory.create(moshi)//todo

    @Singleton @Provides
    fun getScalerConvertor(): ScalarsConverterFactory = ScalarConvertorUtils.getConvertorOrError()

    @Singleton @Provides
    fun getDefaultLogSeverity(): HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY

    @Singleton @Provides
    fun getLogInterceptor(severity: HttpLoggingInterceptor.Level) = LoggingInterceptor.getInstance(severity)

    @Singleton @Provides
    fun getOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpUtils.getClient(loggingInterceptor = loggingInterceptor)

    @Singleton @Provides
    fun getTemporaryBaseUrl(): String = "https://www.google.com"

    @Singleton @Provides
    fun getRetrofitClient(
        baseUrl: String,
        scalarConvertor: ScalarsConverterFactory,
        moshiConverter: MoshiConverterFactory,
        okHttpClient: OkHttpClient,
    ): Retrofit = RetrofitUtils.getRetrofit(
        baseUrl = baseUrl,
        externalOkHttpClient = okHttpClient,
        externalConvertors = mutableListOf(scalarConvertor,moshiConverter),
        callAdapterFactories = listOf(),
    )



    @Singleton @Provides @NetworkWithHeaders
    fun getHeaderInterceptorWithHeaders(): HeaderInterceptor =//todo
        HeaderInterceptor(hashMapOf(HeaderInterceptor.HEADER_DUMMY_API_AUTH_PAIR))


    @Singleton @Provides @NetworkWithHeaders
    fun getOkHttpClientWithHeaders(
        loggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: HeaderInterceptor
    ): OkHttpClient = OkHttpUtils.getClient(
        loggingInterceptor = loggingInterceptor,
        otherInterceptors = listOf(headerInterceptor)//todo use headers directly
    )

    @Singleton @Provides @NetworkWithHeaders
    fun getRetrofitClientWithHeaders(
        baseUrl: String,
        okHttpClientWithDummyHeaders: OkHttpClient,
        scalarConvertor: ScalarsConverterFactory,
        moshiConverter: MoshiConverterFactory,
    ): Retrofit = RetrofitUtils.getRetrofit(
        baseUrl = baseUrl,
        externalOkHttpClient =okHttpClientWithDummyHeaders,
        externalConvertors = mutableListOf(scalarConvertor,moshiConverter),
    )

    @Qualifier @Retention(AnnotationRetention.BINARY) @Keep
    annotation class NetworkWithHeaders
}