package work.curioustools.jb_mobile.modules.dashboard.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import work.curioustools.jb_mobile.R
import work.curioustools.jb_mobile.databinding.ActivityDashboardBinding
import work.curioustools.jb_mobile.utils.VBHolder
import work.curioustools.jb_mobile.utils.VBHolderImpl
import work.curioustools.jb_mobile.utils.findNavControllerSafe
import work.curioustools.jb_mobile.utils.setNavigationBarColor

class DashboardActivity : AppCompatActivity(),
    VBHolder<ActivityDashboardBinding> by VBHolderImpl() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityDashboardBinding.inflate(layoutInflater).setContentView(this)
        setNavigationBarColor(R.color.navbar_color)

        withBinding {
            PopupMenu(root.context, null).let {
                it.inflate(R.menu.menu_dashboard)
                val menu = it.menu
                val controller = findNavControllerSafe(R.id.dashboardNavHost)

                if (menu != null) {
                    navBar.setupWithNavController(menu,controller)
                }
            }

        }
    }
}



