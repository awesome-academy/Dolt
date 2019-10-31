package com.sun.doitpat.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.sun.doitpat.R
import com.sun.doitpat.base.SwipeBindingViewHolder
import com.sun.doitpat.data.model.ToDo
import com.sun.doitpat.util.Constants.COMPLETED
import kotlinx.android.synthetic.main.item_reminder_swipe.view.*

class ToDoSwipeAdapter(private val listener: OnSwipeItem) : RecyclerSwipeAdapter<SwipeBindingViewHolder<ToDo>>() {

    private var items = mutableListOf<ToDo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeBindingViewHolder<ToDo> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
                DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return SwipeBindingViewHolder(binding)
    }

    override fun getItemViewType(position: Int) = R.layout.item_reminder_swipe

    override fun getSwipeLayoutResourceId(position: Int) = R.id.layoutSwipe

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: SwipeBindingViewHolder<ToDo>, position: Int) {

        holder.bind(items[position])
        holder.itemView.apply {

            if (items[position].status == COMPLETED) {
                layoutSwipe.addDrag(SwipeLayout.DragEdge.Right, this.findViewById(R.id.layoutUndo))
                buttonUndo.setOnClickListener {
                    listener.onClickUndo(items[position])
                    holder.close()
                    removeItem(position)
                }
            } else {
                layoutSwipe.addDrag(SwipeLayout.DragEdge.Right, this.findViewById(R.id.layoutComplete))
                buttonComplete.setOnClickListener {
                    listener.onClickComplete(items[position])
                    holder.close()
                    removeItem(position)
                }
            }

            buttonDelete.setOnClickListener {
                listener.onClickDelete(items[position])
                holder.close()
                removeItem(position)
            }

            layoutSurface.setOnClickListener {
                listener.onClickDetail(items[position])
            }
        }
    }

    private fun removeItem(position: Int) {
        items.remove(items[position])
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size - position + OFF_SET)
    }

    fun submitList(items: List<ToDo>) {
        this.items = items.toMutableList()
        notifyDataSetChanged()
    }

    interface OnSwipeItem {
        fun onClickDelete(item: ToDo)
        fun onClickComplete(item: ToDo)
        fun onClickUndo(item: ToDo)
        fun onClickDetail(item: ToDo)
    }

    companion object {
        private const val OFF_SET = 1
    }
}
