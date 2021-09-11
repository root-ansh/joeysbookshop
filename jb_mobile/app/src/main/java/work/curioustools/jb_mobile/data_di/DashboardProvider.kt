package work.curioustools.jb_mobile.data_di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import work.curioustools.jb_mobile.data_apis.DashboardApi
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
class DashboardProvider {

    @Singleton
    @Provides
    fun getBookListApi(retrofit: Retrofit): DashboardApi {
        return retrofit.newBuilder().baseUrl(DashboardApi.BASE_URL).build()
            .create(DashboardApi::class.java)
    }
}