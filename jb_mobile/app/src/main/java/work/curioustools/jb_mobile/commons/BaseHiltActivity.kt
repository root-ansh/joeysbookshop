package work.curioustools.jb_mobile.commons

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater

import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import work.curioustools.jetpack_lifecycles.VBHolder
import work.curioustools.jetpack_lifecycles.VBHolderImpl

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

abstract class BaseCommonActivity:AppCompatActivity(){  //todo core-android-hilt

    val activityHandler: Handler by lazy {
        val looper = Looper.getMainLooper()
        Handler(looper)
    }
}
abstract class BaseCommonActivityVB<VB:ViewBinding>:BaseCommonActivity(), VBHolder<VB> by VBHolderImpl() {
    override fun onCreate(savedInstanceState: Bundle? ) {
        super.onCreate(savedInstanceState)
        getBindingForComponent(layoutInflater).setContentViewFor(this)
        setup()

    }
    abstract fun getBindingForComponent(layoutInflater: LayoutInflater):VB
    abstract fun setup()
}
