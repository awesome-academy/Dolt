package com.sun.doitpat.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.SearchView
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
import com.sun.doitpat.widget.ToDoWidgetProvider
import kotlinx.android.synthetic.main.fragment_main.*

class ToDoFragment : BaseFragment<FragmentMainBinding,
        ToDoViewModel>(),
        ToDoSwipeAdapter.OnSwipeItem,
        SearchView.OnQueryTextListener {

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
        setHasOptionsMenu(true)
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
        })
        viewModel.widgetList.observe(viewLifecycleOwner, Observer {
            broadcastToWidget(ArrayList(it))
        })
        setEventsClick()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val searchMenuItem = menu.findItem(R.id.action_search_to_do)
        (searchMenuItem?.actionView as SearchView).setOnQueryTextListener(this@ToDoFragment)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        swipeAdapter.filter(newText.toString())
        return true
    }

    override fun onQueryTextSubmit(query: String?) = false

    override fun onResume() {
        super.onResume()
        viewModel.getNoAlertToDo()
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
                    NO_ALERT_TAB -> getNoAlertToDo()
                    ALERT_TAB -> getAlertToDo()
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
        context?.let {
            items?.let { _items -> activity?.sendBroadcast(ToDoWidgetProvider.getIntent(it, _items)) }
        }
    }

    private fun getNoAlertToDo() {
        viewModel.getNoAlertToDo()
    }

    private fun getAlertToDo() {
        viewModel.getAlertToDo()
    }

    private fun getCompletedToDo() {
        viewModel.getCompletedToDo()
    }

    companion object {
        private const val NO_ALERT_TAB = 0
        private const val ALERT_TAB = 1
        private const val COMPLETED_TAB = 2
    }
}
