package work.curioustools.jb_mobile.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar


fun View.showKeyboardFromView() = (context as? AppCompatActivity).showKeyboard()//todo verify
fun View.hideKeyboardFromView() = (context as? AppCompatActivity).hideKeyboard()//todo verify
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

fun View.showSnackBar(
    msg: String = "",
    @StringRes msgRes: Int = -1,
    length: Int = Snackbar.LENGTH_SHORT
){
    val finalMsg = if (msgRes == -1) msg else context.getString(msgRes)
    Snackbar.make(this, finalMsg, length).show()
}
