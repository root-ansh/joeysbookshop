package work.curioustools.jb_mobile.modules.dashboard.ui_views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper

import work.curioustools.jb_mobile.commons.BaseHiltActivity
import work.curioustools.jb_mobile.databinding.ActivitySplashBinding
import work.curioustools.jetpack_lifecycles.VBHolder
import work.curioustools.jetpack_lifecycles.VBHolderImpl


@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseHiltActivity(), VBHolder<ActivitySplashBinding> by VBHolderImpl() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivitySplashBinding.inflate(layoutInflater).setContentViewFor(this)
        Handler(Looper.getMainLooper()).postDelayed({

            runOnUiThread {
                finish()
                startActivity(Intent(this, DashboardActivity::class.java))

            }
        }, 2000)
    }

}