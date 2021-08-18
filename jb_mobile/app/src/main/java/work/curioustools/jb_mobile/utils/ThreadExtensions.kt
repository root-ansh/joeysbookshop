package work.curioustools.jb_mobile.utils

fun Thread.logThreadInfo(){
    try {
        this.let {
            println("<current thread info> ------------------")
            println("thread = $it")
            println(" ID = ${it.id}")
            println(" name = ${it.name}")
            println(" state = ${it.state}")
            println(" isAlive = ${it.isAlive}")
            println(" isDaemon = ${it.isDaemon}")
            println(" isInterrupted = ${it.isInterrupted}")
            println("</current thread info> ------------------")
        }
    }
    catch (t:Throwable){
        t.printStackTrace()
    }
}