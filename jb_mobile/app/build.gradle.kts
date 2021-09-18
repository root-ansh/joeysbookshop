plugins {
    GradleConfigs.AppModule.plugins().forEach {
        this.id(it)
    }
}
android.compileSdk = GradleConfigs.AppModule.sdkForCompile

android.defaultConfig.let {
    it.applicationId = GradleConfigs.AppModule.appPackage
    it.minSdk = GradleConfigs.AppModule.sdkMin
    it.targetSdk = GradleConfigs.AppModule.sdkMax
    it.versionCode = GradleConfigs.AppModule.currentVersionCode
    it.versionName = GradleConfigs.AppModule.currentVersionName
    it.testInstrumentationRunner = GradleConfigs.AppModule.testRunner
    it.vectorDrawables.useSupportLibrary = GradleConfigs.AppModule.enableVectorSupport
}
GradleConfigs.AppModule.allBuildTypes.forEach { bt ->
    val defaultProguard = android.getDefaultProguardFile("proguard-android-optimize.txt")
    //val appRootDir = rootProject.projectDir.absolutePath

    android.buildTypes.getByName(bt.name).also { registeredBT -> //register a buildType
        registeredBT.isMinifyEnabled = bt.enableMinify
        registeredBT.multiDexEnabled = bt.enableMultiDex
        registeredBT.isDebuggable = bt.enableDebugging
        registeredBT.isTestCoverageEnabled = bt.enableTestCoverage
        if (bt.setupProguardConfig) {
            registeredBT.proguardFiles(defaultProguard, "proguard-rules.pro")
        }
        if (bt.setupSigningConfig) {
            //registeredBT.signingConfig(/**get signing configs from somewhere**/)
        }
        android.applicationVariants.all {
            val typeName2 = this.buildType?.name
            val flavor = this.flavorName
            val moveOutsideBuildDir = "../../../../" //todo : will not always work
            val apksDirWithSlash = moveOutsideBuildDir + "apks/build-$typeName2 flavor-$flavor/"

            this.outputs.all {
                (this as? com.android.build.gradle.internal.api.BaseVariantOutputImpl)?.outputFileName =
                        AppBuildTypeConfig.getApkName(typeName2, apksDirWithSlash)
            }
        }
    }
}


android.compileOptions.sourceCompatibility = GradleConfigs.AppModule.sourceJDK
android.compileOptions.targetCompatibility = GradleConfigs.AppModule.targetJSDK

android.kotlinOptions.jvmTarget = GradleConfigs.AppModule.KotlinConfig.sourceJDKForKotlin
android.kotlinOptions.useIR = GradleConfigs.AppModule.KotlinConfig.useNewIRBackend

android.buildFeatures.viewBinding = GradleConfigs.AppModule.enableVB


GradleConfigs.AppModule.addExcludes(android.packagingOptions.resources.excludes)


dependencies {
    GradleConfigs.AppModule.setupDependencies(this)
}

kapt.correctErrorTypes = true
kapt.javacOptions {
    this.option("-Adagger.hilt.android.internal.disableAndroidSuperclassValidation=true")
}