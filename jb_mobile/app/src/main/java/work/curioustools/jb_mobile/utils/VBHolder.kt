package work.curioustools.jb_mobile.utils

import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding


interface VBHolder<B : ViewBinding> {

    var binding: B?
    fun getBindingOrError() = checkNotNull(binding)

    fun usingBinding(block: (binding: B) -> Unit) {
        if (binding == null) {
            error(ERROR_BINDING_IS_NULL)
        } else {
            binding?.let { block.invoke(it) }
        }
    }

    fun withBinding(block: B.() -> Unit) {
        if (binding == null) {
            error(ERROR_BINDING_IS_NULL)
        } else {
            binding?.apply { block.invoke(this) }
        }
    }


    /**
     * Make sure to use this with Fragment.viewLifecycleOwner.
     * requires library : androidx.lifecycle:lifecycle-runtime:$lifecycle_version
     */

    fun B.registeredRoot(fragment:Fragment): View {
        registerForBinding(fragment)
        return this.root
    }

    fun B.setContentView(activity:AppCompatActivity){
        registerForBinding(activity)
        activity.setContentView(this.root)
    }
    fun B.registerForBinding(lifecycleOwner: LifecycleOwner): B {
        this@VBHolder.binding = this
        lifecycleOwner.lifecycle.addObserver(
            object :DefaultLifecycleObserver{
                override fun onDestroy(owner: LifecycleOwner) {
                    owner.lifecycle.removeObserver(this)
                    this@VBHolder.binding = null
                }
            }
        )
        return this
    }


    fun registerBinding(binding: B, lifecycleOwner: LifecycleOwner) {
        this.binding = binding
        Log.e("VIEWBINDING", "registerBinding: called " )

        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                Log.e("VIEWBINDING", "onDestroy: called " )
                owner.lifecycle.removeObserver(this)
                this@VBHolder.binding = null
            }
        })
    }

    companion object {
        //// View Binding Error Messages ////
        const val RES_NO_LAYOUT = 0
        const val ERROR_NO_INFLATED_VIEW =
            "Either provide a layout res or binding view to the base class"
        const val ERROR_ADD_BINDING_TO_LIST =
            "Binding is null or unavailable. Please add an instance of your xml's binding class to given list "
        const val ERROR_BINDING_IS_NULL = "Binding Not Found"
        const val ERROR_BIND_CALL_OUTSIDE_LIFECYCLE =
            "Trying to Access binding outside of lifecycle"
    }
}

class VBHolderImpl<VB : ViewBinding> : VBHolder<VB> {
    override var binding: VB? = null

}
