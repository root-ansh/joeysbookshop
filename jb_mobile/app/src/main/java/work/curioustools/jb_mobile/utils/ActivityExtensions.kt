package work.curioustools.jb_mobile.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT



//activity extensions

fun AppCompatActivity.finishDelayed(millis: Long) {
    Thread {
        Thread.sleep(millis)
        runOnUiThread { finish() }
    }.start()
}

fun AppCompatActivity?.showSnackBarFromActivity(
    rootV: View,
    msg: String = "",
    @StringRes msgRes: Int = -1,
    length: Int = LENGTH_SHORT
) {
    this?:return
    rootV.showSnackBar(msg, msgRes, length)

}






fun AppCompatActivity.hideKeyBoard() {
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = this.currentFocus ?: View(this)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun AppCompatActivity.showToastFromActivity(str:String) = showToast(str)


fun AppCompatActivity.makeUiGoBeyondStatusBarAndBottomNavBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window?.setDecorFitsSystemWindows(false)
    } else {

        window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN or
                    (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) or
                    (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

    }
}


fun AppCompatActivity.setStatusBarColor(@ColorRes res:Int){//todo verify
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return

    window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = getColorCompat(res)
    }
}

fun AppCompatActivity.setNavigationBarColor(@ColorRes barColor: Int , setContrastingNavIcons:Boolean = true) {//todo verify
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return
    window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val barColorCode = getColorCompat(barColor)
        navigationBarColor = barColorCode
       if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
           var flags = decorView.systemUiVisibility
           flags =
               if (isColorLight(barColorCode))
                   (flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
               else
                   (flags and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv())
           decorView.systemUiVisibility = flags
       }
    }
}
fun AppCompatActivity.setStatusBarIconColorAsWhite(setAsWhite: Boolean ) {
    window.decorView.rootView.systemUiVisibility = if (setAsWhite) 0 else 8192
}

fun AppCompatActivity?.showKeyboard() {
    this?:return
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}//todo verify


fun AppCompatActivity?.hideKeyboard() {
    this?:return
    if (currentFocus != null) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

}//todo verify


fun AppCompatActivity.makeUiGoFullScreen() {
    // i.e make ui go Beyond Status Bar And Bottom NavBar
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window?.setDecorFitsSystemWindows(false)
    } else {

        window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

    }
}

fun AppCompatActivity.makeUIGoImmersive() {
    // difference b/w full screen and immersive?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window?.setDecorFitsSystemWindows(false)
    } else {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN
    }
}

fun AppCompatActivity.findNavControllerByID(fragmentId:Int):NavController{
    val navHost = supportFragmentManager.findFragmentById(fragmentId) as NavHostFragment
    return navHost.navController
}

fun AppCompatActivity.findNavControllerByTAG(tag:String):NavController{
    val navHost = supportFragmentManager.findFragmentByTag(tag) as NavHostFragment
    return navHost.navController
}