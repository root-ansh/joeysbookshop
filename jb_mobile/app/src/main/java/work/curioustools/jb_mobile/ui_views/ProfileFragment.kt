package work.curioustools.jb_mobile.ui_views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import work.curioustools.curiousutils.core_droidjet.arch.BaseCommonFragmentVB
import work.curioustools.jb_mobile.databinding.FragmentProfileBinding

class ProfileFragment : BaseCommonFragmentVB<FragmentProfileBinding>() {
    override fun getBindingForThisComponent(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = FragmentProfileBinding.inflate(inflater,container,false)

    override fun setup() {
    }
}