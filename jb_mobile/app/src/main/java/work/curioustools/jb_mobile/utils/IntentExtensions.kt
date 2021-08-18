package work.curioustools.jb_mobile.utils

import android.content.Context
import android.content.Intent



fun Intent.startThisActivity(context: Context) {
    context.startActivity(this)
}