package com.oguzhanaslann.commonui

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import kotlin.properties.ReadOnlyProperty
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KProperty

class ActivityViewBindingDelegate<T : Activity, out V : ViewBinding>(
    private val viewBindingFactory: (T) -> V
) : ReadOnlyProperty<T, V> {

    private var binding: V? = null

    override fun getValue(thisRef: T, property: KProperty<*>): V {
        val binding = binding
        if (binding != null) {
            return binding
        }
        return viewBindingFactory(thisRef).also { this.binding = it }
    }
}

fun<V : ViewBinding> Activity.activityViewBinding(viewBindingFactory: (Activity) -> V) =
    ActivityViewBindingDelegate(viewBindingFactory)


fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T) =
    FragmentViewBindingDelegate(this, viewBindingFactory)

class FragmentViewBindingDelegate<T : ViewBinding>(
    val fragment: Fragment,
    val viewBindingFactory: (View) -> T
) : ReadOnlyProperty<Fragment, T> {
    private var binding: T? = null

    init {
        val viewLifecycleOwnerLiveDataObserver =
            Observer<LifecycleOwner?> {
                val viewLifecycleOwner = it ?: run {
                    binding = null
                    return@Observer
                }

                //binding reference set to be null when view life cycle is destroyed
                viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                    override fun onDestroy(owner: LifecycleOwner) {
                        binding = null
                    }
                })
            }

        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observeForever(
                    viewLifecycleOwnerLiveDataObserver
                )
            }

            override fun onDestroy(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.removeObserver(
                    viewLifecycleOwnerLiveDataObserver
                )
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val binding = binding
        if (binding != null) {
            return binding
        }

        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException("Should not attempt to get bindings when Fragment views are destroyed: ${fragment.javaClass.simpleName}")
        }

        return viewBindingFactory(thisRef.requireView()).also { this.binding = it }
    }
}
