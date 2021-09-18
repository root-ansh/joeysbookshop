//@com.android.tools.r8.Keep // not accessible here. i wonder what to do
data class GradleRootTasks<T>(val name:String,val type:Class<T>,val action: T.() -> Unit = {})