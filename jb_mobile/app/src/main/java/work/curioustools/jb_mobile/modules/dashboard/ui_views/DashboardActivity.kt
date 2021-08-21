package work.curioustools.jb_mobile.modules.dashboard.ui_views

import android.os.Bundle
import android.widget.PopupMenu
import work.curioustools.jb_mobile.R
import work.curioustools.jb_mobile.commons.BaseHiltActivity
import work.curioustools.jb_mobile.databinding.ActivityDashboardBinding
import work.curioustools.jb_mobile.utils.*

class DashboardActivity : BaseHiltActivity(),
    VBHolder<ActivityDashboardBinding> by VBHolderImpl() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityDashboardBinding.inflate(layoutInflater).setContentView(this)
        setSystemBottomNavBarColor(R.color.navbar_color)
        toggleActionBar(false)
        setStatusBarIconColorAsWhite(isDarkThemeOn())

        withBinding {
            PopupMenu(root.context, null).let {
                it.inflate(R.menu.menu_dashboard)
                val menu = it.menu
                val hostTag = (getString(R.string.host_fragment_tag))
                val controller = findNavControllerByTAG(hostTag)

                if (menu != null) {
                    navBar.setupWithNavController(menu,controller)
                }
            }

        }
    }
}


