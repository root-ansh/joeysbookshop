package work.curioustools.jb_mobile.commons

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import work.curioustools.curiousutils.core_droidjet.vb_helpers.VBHolder
import work.curioustools.curiousutils.core_droidjet.vb_helpers.VBHolderImpl

@AndroidEntryPoint
abstract class BaseHiltActivity:AppCompatActivity(){//todo core-android-hilt

    val activityHandler: Handler by lazy {
        val looper = Looper.getMainLooper()
        Handler(looper)
    }
}

abstract class BaseHiltActivityVB<VB:ViewBinding>:BaseHiltActivity(), VBHolder<VB> by VBHolderImpl() {
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         getBindingForComponent(layoutInflater).setContentViewFor(this)
         setup()
     }

     abstract fun getBindingForComponent(layoutInflater: LayoutInflater):VB
     abstract fun setup()

 }

@AndroidEntryPoint
abstract class BaseHiltFragment : Fragment()

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
