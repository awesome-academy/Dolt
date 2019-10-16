package com.sun.doitpat.base

import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView

class BindingViewHolder<T>(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

    fun bind(item: T) {
        binding.apply {
            setVariable(BR.todo, item)
            executePendingBindings()
        }
    }
}
