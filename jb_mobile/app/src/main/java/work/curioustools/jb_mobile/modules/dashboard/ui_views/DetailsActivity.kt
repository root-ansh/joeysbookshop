package work.curioustools.jb_mobile.modules.dashboard.ui_views

import android.os.Bundle
import work.curioustools.jb_mobile.commons.BaseHiltActivity
import work.curioustools.jb_mobile.databinding.ActivityDetailsBinding
import work.curioustools.jb_mobile.utils.VBHolder
import work.curioustools.jb_mobile.utils.VBHolderImpl

class DetailsActivity : BaseHiltActivity(),VBHolder<ActivityDetailsBinding> by VBHolderImpl(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityDetailsBinding.inflate(layoutInflater).setContentViewFor(this)
    }
}