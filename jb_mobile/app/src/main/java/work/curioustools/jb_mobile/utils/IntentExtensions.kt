package work.curioustools.jb_mobile.utils

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity


fun Intent.startThisActivity(context: Context) {
    context.startActivity(this)
}
fun Intent.startThisActivityForResult(activity: AppCompatActivity?, rc:Int) {
    activity?.startActivityForResult(this,rc)
}
