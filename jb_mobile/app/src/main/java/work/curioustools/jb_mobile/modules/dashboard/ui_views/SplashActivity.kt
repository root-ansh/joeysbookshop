package work.curioustools.jb_mobile.modules.dashboard.ui_views

import android.content.Intent
import android.view.LayoutInflater
import work.curioustools.core_android.*
import work.curioustools.jb_mobile.R
import work.curioustools.jb_mobile.commons.BaseCommonActivityVB
import work.curioustools.jb_mobile.databinding.ActivitySplashBinding

class SplashActivity : BaseCommonActivityVB<ActivitySplashBinding>(){
    override fun getBindingForComponent(layoutInflater: LayoutInflater)= ActivitySplashBinding.inflate(layoutInflater)

    override fun setup() {
        setSystemBottomNavBarColor(R.color.black)           //todo systemSetColorForBottomNavBar()
        toggleActionBar(false)                        //todo systemToggleThemeProvidedActionBar()
        setStatusBarIconColorAsWhite(isDarkThemeOn())       //todo systemSetStatusBarIconsAsWhite()
        setStatusBarColor(R.color.black)

        activityHandler.postDelayed({

            runOnUiThread {
                finish()
                startActivity(Intent(this, DashboardActivity::class.java))

            }
        }, 2000)
    }

}