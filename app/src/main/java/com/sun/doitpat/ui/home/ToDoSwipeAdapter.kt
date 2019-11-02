package com.sun.doitpat.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.sun.doitpat.R
import com.sun.doitpat.base.SwipeBindingViewHolder
import com.sun.doitpat.data.model.ToDo
import com.sun.doitpat.util.Constants.ALERT
import com.sun.doitpat.util.Constants.COMPLETED
import com.sun.doitpat.util.Constants.EMPTY_STRING
import com.sun.doitpat.util.isContains
import com.sun.doitpat.util.isLaterThanNow
import kotlinx.android.synthetic.main.item_reminder_swipe.view.*

class ToDoSwipeAdapter(private val listener: OnSwipeItem) : RecyclerSwipeAdapter<SwipeBindingViewHolder<ToDo>>() {

    private var items = mutableListOf<ToDo>()
    private var filteredItems = mutableListOf<ToDo>()
    private var filterItems = listOf<ToDo>()
    private var currentFilterText = EMPTY_STRING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeBindingViewHolder<ToDo> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return SwipeBindingViewHolder(binding)
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = R.layout.item_reminder_swipe

    override fun getSwipeLayoutResourceId(position: Int) = R.id.layoutSwipe

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: SwipeBindingViewHolder<ToDo>, position: Int) {

        holder.bind(items[position])
        holder.itemView.apply {
            items[position].createdTimeMillisecond?.let {
                textAlarmSign.visibility =
                    if ((it.isLaterThanNow()) &&
                        (items[position].alertStatus == ALERT))
                        View.VISIBLE else View.GONE
            }
            buttonComplete.visibility = View.GONE
            buttonUndo.visibility = View.GONE

            if (items[position].status == COMPLETED) {
                buttonUndo.visibility = View.VISIBLE
                layoutSwipe.addDrag(SwipeLayout.DragEdge.Right, this.findViewById(R.id.layoutUndo))
                buttonUndo.setOnClickListener {
                    listener.onClickUndo(items[position])
                    holder.close()
                    removeItem(position)
                }
            } else {
                buttonComplete.visibility = View.VISIBLE
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

    private fun submitFilterList(items: List<ToDo>) {
        this.items = items.toMutableList()
        notifyDataSetChanged()
    }

    private fun refreshList() {
        this.items = filterItems.toMutableList()
        notifyDataSetChanged()
    }

    fun submitList(items: List<ToDo>) {
        this.items = items.toMutableList()
        filterItems = items
        filter(currentFilterText)
        notifyDataSetChanged()
    }

    fun filter(filterString: String) {
        refreshList()
        filteredItems =
            items.filter { it.information.isContains(filterString) }.toMutableList()
        submitFilterList(filteredItems)
        currentFilterText = filterString
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
