package com.sun.doitpat.ui.home

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.sun.doitpat.BR
import com.sun.doitpat.R
import com.sun.doitpat.base.BaseFragment
import com.sun.doitpat.base.ViewModelFactory
import com.sun.doitpat.data.model.ToDo
import com.sun.doitpat.data.repository.ToDoRepository
import com.sun.doitpat.data.repository.impl.ToDoRepositoryImpl
import com.sun.doitpat.data.source.local.AppDatabase
import com.sun.doitpat.databinding.FragmentMainBinding
import com.sun.doitpat.util.Constants.DEFAULT_ID
import com.sun.doitpat.util.Constants.WIDGET_BROADCAST
import com.sun.doitpat.widget.ToDoWidgetProvider
import kotlinx.android.synthetic.main.fragment_main.*

class ToDoFragment : BaseFragment<FragmentMainBinding, ToDoViewModel>(), ToDoSwipeAdapter.OnSwipeItem {

    override val layoutId: Int
        get() = R.layout.fragment_main
    override val bindingVariable: Int
        get() = BR.list
    override val viewModel: ToDoViewModel
        get() = toDoViewModel

    private val toDoRepository by lazy {
        context?.let { ToDoRepositoryImpl(AppDatabase.invoke(it).toDoDao()) as ToDoRepository }
    }

    private lateinit var toDoViewModel: ToDoViewModel
    private val swipeAdapter by lazy { ToDoSwipeAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = swipeAdapter
        }
        viewModel.list.observe(viewLifecycleOwner, Observer {
            swipeAdapter.submitList(it)
            broadcastToWidget(ArrayList(it))
        })
        setEventsClick()

    }

    override fun onResume() {
        super.onResume()
        viewModel.getNewToDo()
    }

    override fun onClickComplete(item: ToDo) {
        viewModel.updateItem(item)
    }

    override fun onClickDelete(item: ToDo) {
        viewModel.deleteItem(item)
    }

    override fun onClickUndo(item: ToDo) {
        viewModel.undoItem(item)
    }

    override fun onClickDetail(item: ToDo) {
        val bundle = Bundle()
        bundle.putInt(resources.getString(R.string.title_title), item.id)
        view?.findNavController()?.navigate(R.id.detailFragment, bundle)
    }

    private fun createViewModel() {
        toDoRepository?.let {
            toDoViewModel = ViewModelProviders.of(
                    this,
                    ViewModelFactory { ToDoViewModel(it) }).get(ToDoViewModel::class.java)
        }
    }

    private fun setEventsClick() {

        cardAddItem?.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(resources.getString(R.string.title_title), DEFAULT_ID)
            view?.findNavController()?.navigate(R.id.detailFragment, bundle)
        }

        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    NEW_TAB -> getNewToDo()
                    COMPLETED_TAB -> getCompletedToDo()
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private fun broadcastToWidget(items: ArrayList<ToDo>?) {

        val intent = Intent(context, ToDoWidgetProvider::class.java)
        intent.action = WIDGET_UPDATE_ACTION

        context?.applicationContext?.let {
            val widgetIdArray = AppWidgetManager.getInstance(it)
                    .getAppWidgetIds(ComponentName(it, ToDoWidgetProvider::class.java))
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIdArray)
            intent.putParcelableArrayListExtra(WIDGET_BROADCAST, items)
            activity?.sendBroadcast(intent)
        }

    }

    private fun getNewToDo() {
        viewModel.getNewToDo()
    }

    private fun getCompletedToDo() {
        viewModel.getCompletedToDo()
    }

    companion object {
        private const val NEW_TAB = 0
        private const val COMPLETED_TAB = 2
        private const val WIDGET_UPDATE_ACTION = "android.appwidget.action.APPWIDGET_UPDATE"
    }
}
