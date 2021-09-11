package work.curioustools.jb_mobile.commons

fun String?.toIntSafe(default:Int = 0):Int{//todo move to package
    if(this == null) return default
    return try {
        this.toInt()
    }
    catch (t:Throwable){
        t.printStackTrace()
        default
    }
}