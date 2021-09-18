import Dependencies.AndroidAndJetPackDependencies.hiltVer
import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {

    object ModulePlugins {
        private const val APPLICATION = "com.android.application"
        private const val KOTLIN_ANDROID = "kotlin-android"
        private const val KOTLIN_KAPT = "kotlin-kapt"
        private const val KOTLIN_PARCELIZE = "kotlin-parcelize"
        private const val ANDROID_HILT = "dagger.hilt.android.plugin"


        fun forApplication() = arrayOf(APPLICATION, KOTLIN_ANDROID, KOTLIN_KAPT, KOTLIN_PARCELIZE, ANDROID_HILT )
    }


    object KotlinDependencies {
        private const val ktx = "androidx.core:core-ktx:1.6.0" //todo remove, coming from core_droidjet

        fun addAllTo(depsList: DependencyHandler) {
            depsList.add("implementation", ktx)
        }

    }

    object AndroidAndJetPackDependencies {

        private const val multidex = ("androidx.multidex:multidex:2.0.1")//todo move to core_droidjet

        const val hiltVer = "2.38.1"
        private const val daggerHilt = "com.google.dagger:hilt-android:$hiltVer"
        private const val daggerHiltCompiler = "com.google.dagger:hilt-android-compiler:$hiltVer"

        fun addAllTo(depsList: DependencyHandler) {
            arrayOf(multidex, daggerHilt).forEach {
                depsList.add("implementation", it)
            }

            depsList.add("kapt", daggerHiltCompiler)
        }
    }

    object CuriousUtilsDependencies {
        private const val androidAndJetPack = "com.github.curioustools.curiousutils:core_droidjet:2021.09.18"
        private const val jdk = "com.github.curioustools.curiousutils:core_jdk:2021.09.18"
        private const val network = "com.github.curioustools.curiousutils:third_party_network:2021.09.18"

        fun addAllTo(depsList: DependencyHandler) {
            arrayOf(androidAndJetPack, jdk, network).forEach { depsList.add("implementation", it) }
        }

    }

    object ThirdPartyUiDependencies {
        private const val googleFlexBox = "com.google.android.flexbox:flexbox:3.0.0"
        private const val shimmer = "com.facebook.shimmer:shimmer:0.5.0"
        private const val browser = "androidx.browser:browser:1.3.0"
        private const val smoothBottomBar = "com.github.ibrahimsn98:SmoothBottomBar:1.7.8"
        fun addAllTo(depsList: DependencyHandler) {
            arrayOf(googleFlexBox,browser,shimmer,smoothBottomBar).forEach { depsList.add("implementation",it) }
        }
    }


    object TestingDependencies {
        fun addAllTo(depsList: DependencyHandler) {
            depsList.run {
                add("testImplementation", "junit:junit:4.13.2")
                add("androidTestImplementation", "androidx.test.ext:junit:1.1.3")
                add("androidTestImplementation", "androidx.test.espresso:espresso-core:3.4.0")
            }
        }
    }


    object ProjectRootDependencies {
        private const val gradlePlugin = "com.android.tools.build:gradle:7.0.2"// must be compatible with gradle wrapper version at `/gradle/wrapper/gradle-wrapper.properties`
        private const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21"
        private const val hiltPlugin = "com.google.dagger:hilt-android-gradle-plugin:$hiltVer"
        fun all() = listOf(gradlePlugin, kotlinGradlePlugin, hiltPlugin)
    }


}
/*

kapt {
    correctErrorTypes = true
    javacOptions {
        option("-Adagger.hilt.android.internal.disableAndroidSuperclassValidation=true")
    }
}

* */
