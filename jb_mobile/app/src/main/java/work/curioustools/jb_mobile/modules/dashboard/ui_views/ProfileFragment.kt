package work.curioustools.jb_mobile.modules.dashboard.ui_views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import work.curioustools.jb_mobile.databinding.FragmentProfileBinding
import work.curioustools.jetpack_lifecycles.VBHolder
import work.curioustools.jetpack_lifecycles.VBHolderImpl


class ProfileFragment : Fragment(), VBHolder<FragmentProfileBinding> by VBHolderImpl() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentProfileBinding.inflate(inflater, container, false).registeredRoot(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}