package com.sun.doitpat.base

import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.sun.doitpat.R
import kotlinx.android.synthetic.main.item_reminder_swipe.view.*

class SwipeBindingViewHolder<T>(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

    fun bind(item: T) {
        binding.apply {
            setVariable(BR.todo, item)
            executePendingBindings()
        }
    }

    init {
        itemView.layoutSwipe.apply {
            addDrag(SwipeLayout.DragEdge.Left, this.findViewById(R.id.layoutDelete))
        }
    }

    fun close() {
        itemView.layoutSwipe.close(true)
    }

}
