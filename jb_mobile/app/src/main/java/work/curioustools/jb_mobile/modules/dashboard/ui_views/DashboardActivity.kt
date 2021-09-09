package work.curioustools.jb_mobile.modules.dashboard.ui_views

import android.content.Intent
import android.os.Bundle
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import work.curioustools.core_android.currentNavigationFragment
import work.curioustools.core_android.findNavControllerByTAG
import work.curioustools.core_android.showToastFromActivity
import work.curioustools.jb_mobile.R
import work.curioustools.jb_mobile.commons.BaseHiltActivity
import work.curioustools.jb_mobile.databinding.ActivityDashboardBinding
import work.curioustools.jetpack_lifecycles.VBHolder
import work.curioustools.jetpack_lifecycles.VBHolderImpl

class DashboardActivity : BaseHiltActivity(), VBHolder<ActivityDashboardBinding> by VBHolderImpl() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityDashboardBinding.inflate(layoutInflater).setContentViewFor(this)

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


