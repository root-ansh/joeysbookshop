package work.curioustools.jb_mobile.commons

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import work.curioustools.jb_mobile.R
import work.curioustools.jb_mobile.utils.isDarkThemeOn
import work.curioustools.jb_mobile.utils.setStatusBarIconColorAsWhite
import work.curioustools.jb_mobile.utils.setSystemBottomNavBarColor
import work.curioustools.jb_mobile.utils.toggleActionBar

@AndroidEntryPoint
abstract class BaseHiltActivity:AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSystemBottomNavBarColor(R.color.navbar_color)
        toggleActionBar(false)
        setStatusBarIconColorAsWhite(isDarkThemeOn())
    }
}
