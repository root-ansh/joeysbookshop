@file:Suppress("unused", "UnusedImport", "BooleanLiteralArgument")//10
import Dependencies.ModulePlugins
import org.gradle.api.JavaVersion
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.initialization.dsl.ScriptHandler
import org.gradle.api.initialization.resolve.RepositoriesMode
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.TaskContainer
import org.gradle.internal.component.external.model.ComponentVariant
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.support.delegates.SettingsDelegate
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object GradleConfigs {

    object AppModule { //ProjectApp

        fun plugins() = ModulePlugins.forApplication()
        val allBuildTypes = arrayOf(AppBuildTypeConfig.BT_DEBUG,AppBuildTypeConfig.BT_RELEASE )

        //BuildInfo
        const val sdkMin = 21
        const val sdkMax = 31
        const val sdkForCompile = 31
        const val currentVersionCode = 2
        const val currentVersionName = "2021.09.19--$currentVersionCode"
        const val appName = "jb_mobile"
        const val appPackage = "work.curioustools.$appName"
        const val testRunner = "androidx.test.runner.AndroidJUnitRunner"


        val sourceJDK = JavaVersion.VERSION_1_8
        val targetJSDK = JavaVersion.VERSION_1_8

        object KotlinConfig {
            const val sourceJDKForKotlin = "1.8"
            const val useNewIRBackend = true
        }

        const val enableVB = true
        const val enableVectorSupport =true



        fun addExcludes(set: MutableSet<String>) {
            set += "/META-INF/{AL2.0,LGPL2.1}"
        }

        fun setupDependencies(dependencyHandlerScope: DependencyHandlerScope) {
            dependencyHandlerScope{
                Dependencies.KotlinDependencies.addAllTo(this)
                Dependencies.AndroidAndJetPackDependencies.addAllTo(this)
                Dependencies.TestingDependencies.addAllTo(this)
                Dependencies.CuriousUtilsDependencies.addAllTo(this)
                Dependencies.ThirdPartyUiDependencies.addAllTo(this)
            }
        }
    }

    object ProjectRoot {

        // static functions --->--->--->--->--->--->--->--->--->--->--->--->--->--->--->--->--->--->--->--->--->--->--->--->--->

        fun setupRootRepos(repositoryHandler: RepositoryHandler) {
            val repos = mutableListOf(
                repositoryHandler.google(),
                repositoryHandler.mavenCentral(),
                repositoryHandler.maven(url = "https://jitpack.io")
            )
            repos.forEach { repositoryHandler.add(it) }
        }

        fun setupClassPathDependencies(dependencyHandler: DependencyHandler) {
            // adds classpath dependencies(plugins) to DependencyHandler
            Dependencies.ProjectRootDependencies.all().forEach {
                dependencyHandler.add(ScriptHandler.CLASSPATH_CONFIGURATION, it) //aka classpath("some.deps")
            }
        }

        fun setupTasks(taskContainer: TaskContainer, buildDir: File) {
            //customTasks, run automatically on build
            val deleteTask = GradleRootTasks("clean", Delete::class.java) { this.delete(buildDir) }
            taskContainer.register(deleteTask.name, deleteTask.type, deleteTask.action)

        }



    }



}