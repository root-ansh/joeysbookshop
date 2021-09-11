package work.curioustools.jb_mobile.commons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import work.curioustools.jb_mobile.databinding.FragmentDashboardBinding
import work.curioustools.jetpack_lifecycles.VBHolder
import work.curioustools.jetpack_lifecycles.VBHolderImpl

@AndroidEntryPoint
abstract class BaseHiltFragment : Fragment() {
}

abstract class BaseHiltFragmentVB<VB : ViewBinding> :
    BaseHiltFragment(),
    VBHolder<VB> by VBHolderImpl() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getBindingForThisComponent(inflater, container, savedInstanceState).registeredRoot(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    abstract fun getBindingForThisComponent(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): VB
    abstract fun setup()
}



abstract class BaseCommonFragment : Fragment()


abstract class BaseCommonFragmentVB<VB : ViewBinding> :
    BaseCommonFragment(),
    VBHolder<VB> by VBHolderImpl() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getBindingForThisComponent(inflater, container, savedInstanceState).registeredRoot(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    abstract fun getBindingForThisComponent(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): VB
    abstract fun setup()
}
