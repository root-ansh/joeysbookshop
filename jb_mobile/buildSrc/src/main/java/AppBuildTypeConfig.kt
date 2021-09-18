import java.text.SimpleDateFormat
import java.util.*

data class AppBuildTypeConfig(
    val name: String,

    //props that should be enabled for debugging
    val enableDebugging: Boolean = false,
    val enableTestCoverage: Boolean = false,

    // props that should be enabled for release/final apks (&disabled for debug)
    val enableMinify: Boolean = true,
    val setupProguardConfig: Boolean = true,
    val setupSigningConfig:Boolean = true,

    // props that should always be same for all builds
    val enableMultiDex: Boolean = true,
) {

    companion object {
         val BT_DEBUG = AppBuildTypeConfig(
            name = "debug",
            enableDebugging = true,
            enableTestCoverage = true,

            enableMinify = false,
            setupProguardConfig = false,
            setupSigningConfig = false,
        )

        // build kts provides debug and release by default , so getBName("debug") can run on these. but to use any other name, we have to use create("staging") or maybeCreate("staging")
         val BT_STAGING = AppBuildTypeConfig(
            name = "staging",
        )

         val BT_RELEASE = AppBuildTypeConfig(
            name = "release",
        )

        fun getApkName(typeName: String?, dirWithSlash:String): String {
            val time = kotlin
                .runCatching { SimpleDateFormat("yyyy-mm-dd HH-mm", Locale.ROOT).format(Date())  }
                .getOrNull() ?: "${System.currentTimeMillis()}"

            val appName = GradleConfigs.AppModule.appName
            val version = GradleConfigs.AppModule.currentVersionName

            val finalName = "${dirWithSlash}$time/$appName.$typeName-$version.apk"
            println(finalName)
            return finalName

        }


    }

}

//misc learnings:
// - type name is always receiving previous typename. like if we created dev, then its giving dev even after converting to release. so clean is required after every build
// - flavor is coming as empty if not set
// - there is no point in time stamping folders or apks as gradle build task clears all the older folder structure/builds.  however if generating builds outside the build folder , then that persists
// - need to use all{} {} with variants and outputs . dont' know why, but it won't work withut those