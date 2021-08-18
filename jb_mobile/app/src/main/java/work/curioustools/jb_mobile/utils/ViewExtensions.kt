package work.curioustools.jb_mobile.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


fun View.showKeyboard() = (context as? AppCompatActivity).showKeyboard()//todo verify
fun View.hideKeyboard() = (context as? AppCompatActivity).hideKeyboard()//todo verify
fun View?.showKeyboardForced(act: AppCompatActivity?) {
    this ?: return
    val view = this
    act?.run {
        if (act.currentFocus != null) {
            val inputMethodManager = act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED)
        }
    }
}

fun View.showToastFromView(str:String) =   context?.showToast(str)



