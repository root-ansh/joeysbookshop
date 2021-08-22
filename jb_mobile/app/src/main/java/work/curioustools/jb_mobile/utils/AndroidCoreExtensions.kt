package work.curioustools.jb_mobile.utils

import android.graphics.Color

fun Any.isColorLight(colorCode: Int): Boolean {
    val darkness = 1 - (0.299 * Color.red(colorCode) + 0.587 * Color.green(colorCode) + 0.114 * Color.blue(colorCode)) / 255
    return darkness < 0.5
}
