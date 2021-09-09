package work.curioustools.jb_mobile.utils

fun String?.toIntSafe(default:Int = 0):Int{
    if(this == null) return default
    return try {
        this.toInt()
    }
    catch (t:Throwable){
        t.printStackTrace()
        default
    }
}