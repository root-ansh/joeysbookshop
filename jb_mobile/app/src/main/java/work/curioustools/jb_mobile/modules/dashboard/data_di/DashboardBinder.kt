package work.curioustools.jb_mobile.modules.dashboard.data_di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import work.curioustools.jb_mobile.modules.dashboard.data_repos.DashboardRepo
import work.curioustools.jb_mobile.modules.dashboard.data_repos.DashboardRepoImpl


@Module
@InstallIn(ViewModelComponent::class)
abstract class DashboardBinder {

    @Binds
    @ViewModelScoped
    abstract fun bindAuthRepo(dashboardRepoImpl: DashboardRepoImpl): DashboardRepo




}