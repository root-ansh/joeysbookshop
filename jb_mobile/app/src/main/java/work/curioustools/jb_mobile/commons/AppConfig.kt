package work.curioustools.jb_mobile.commons

import work.curioustools.jb_mobile.BuildConfig


class AppConfig {

    companion object{
        fun isDebug() = BuildConfig.DEBUG
    }
}