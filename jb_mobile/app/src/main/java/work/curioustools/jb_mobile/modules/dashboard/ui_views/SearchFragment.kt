package work.curioustools.jb_mobile.modules.dashboard.ui_views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import work.curioustools.jb_mobile.databinding.FragmentSearchBinding
import work.curioustools.jb_mobile.utils.VBHolder
import work.curioustools.jb_mobile.utils.VBHolderImpl
import work.curioustools.jb_mobile.utils.showToastFromView

class SearchFragment : Fragment(), VBHolder<FragmentSearchBinding> by VBHolderImpl() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSearchBinding.inflate(inflater, container, false).registeredRoot(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getBindingOrError().tvText.setOnClickListener {
            it.showToastFromView("SearchFragment!")
        }
    }
}