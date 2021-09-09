package work.curioustools.jb_mobile.commons

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import work.curioustools.core_android.*
import work.curioustools.jb_mobile.R

@AndroidEntryPoint
abstract class BaseHiltActivity:AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSystemBottomNavBarColor(R.color.navbar_color)
        toggleActionBar(false)
        setStatusBarIconColorAsWhite(isDarkThemeOn())
    }
}
