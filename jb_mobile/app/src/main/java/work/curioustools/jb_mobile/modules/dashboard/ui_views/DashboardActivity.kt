package work.curioustools.jb_mobile.modules.dashboard.ui_views

import android.content.Intent
import android.view.LayoutInflater
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import work.curioustools.core_android.*
import work.curioustools.jb_mobile.R
import work.curioustools.jb_mobile.commons.BaseHiltActivityVB
import work.curioustools.jb_mobile.databinding.ActivityDashboardBinding

class DashboardActivity : BaseHiltActivityVB<ActivityDashboardBinding>(){
    override fun getBindingForComponent(layoutInflater: LayoutInflater)= ActivityDashboardBinding.inflate(layoutInflater)

    override fun setup() {
        setSystemBottomNavBarColor(R.color.navbar_color)           //todo systemSetColorForBottomNavBar()
        toggleActionBar(false)                        //todo systemToggleThemeProvidedActionBar()
        setStatusBarIconColorAsWhite(isDarkThemeOn())       //todo systemSetStatusBarIconsAsWhite()
        setStatusBarColor(R.color.navbar_color)

        withBinding {
            val popupMenu = PopupMenu(root.context, null)
            popupMenu.inflate(R.menu.menu_dashboard)
            val menu = popupMenu.menu
            val hostTag = (getString(R.string.host_fragment_tag))
            val controller = findNavControllerByTAG(hostTag)

            if (menu != null) {
                navBar.setupWithNavController(menu, controller)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == AppCompatActivity.RESULT_OK){
            when(requestCode){
                DetailsActivity.CLOSING_WITH_UPDATE ->{
                    val curFrag= this.supportFragmentManager.currentNavigationFragment

                  if(  curFrag is DashBoardFragment ){
                      (curFrag).initCall()
                  }
                    else if (curFrag is SearchFragment){
                      (curFrag).searchRequest()
                  }
                    else{
                        showToastFromActivity("activity result called for fragment :${curFrag?.let { it::class.java.simpleName }}")
                  }
                }
            }
        }

        //NavHostFragment navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host);
        //navHostFragment.getChildFragmentManager().getFragments().get(0);
    }
}


