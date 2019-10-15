package com.sun.doitpat.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseListAdapter<T>(diffCallback: DiffUtil.ItemCallback<T>) :
        ListAdapter<T, BindingViewHolder<T>>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
                DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder<T>, position: Int) = holder.bind(getItem(position))
}
