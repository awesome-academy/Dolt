package com.sun.doitpat.ui.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.sun.doitpat.BR
import com.sun.doitpat.R
import com.sun.doitpat.base.BaseFragment
import com.sun.doitpat.base.ViewModelFactory
import com.sun.doitpat.data.repository.ToDoRepository
import com.sun.doitpat.data.repository.impl.ToDoRepositoryImpl
import com.sun.doitpat.data.source.local.AppDatabase
import com.sun.doitpat.databinding.FragmentMainBinding
import kotlinx.android.synthetic.main.fragment_main.*

class ToDoFragment : BaseFragment<FragmentMainBinding, ToDoViewModel>() {

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
    private val toDoAdapter by lazy { ToDoAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = toDoAdapter
        }
        toDoViewModel.list.observe(viewLifecycleOwner, Observer {
            toDoAdapter.submitList(it)
        })
        setEventsClick()
    }

    private fun createViewModel() {
        toDoRepository?.let {
            toDoViewModel = ViewModelProviders.of(
                    this,
                    ViewModelFactory { ToDoViewModel(it) }).get(ToDoViewModel::class.java)
        }
    }

    private fun setEventsClick() {
        cardAddItem.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.detailFragment)
        }
    }
}
