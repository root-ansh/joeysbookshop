package work.curioustools.jb_mobile.utils

import android.content.Context
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

fun Context.getColorCompat(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

fun Context.getDrawableCompat(@DrawableRes res: Int) = ContextCompat.getDrawable(this, res)

fun Context.showToast(str: String, length: Int = LENGTH_SHORT) =
    Toast.makeText(this, str, length).show()




