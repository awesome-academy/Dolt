package com.sun.doitpat.base

import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel> : Fragment() {

    @get:LayoutRes
    abstract val layoutId: Int

    private var rootView: View? = null
    private var viewDataBinding: T? = null
    abstract val bindingVariable: Int
    abstract val viewModel: V

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        rootView = viewDataBinding?.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.apply {
            setVariable(bindingVariable, viewModel)
            lifecycleOwner = viewLifecycleOwner
            executePendingBindings()
        }
    }
}
