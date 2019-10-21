package com.sun.doitpat.ui

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sun.doitpat.BR
import com.sun.doitpat.R
import com.sun.doitpat.base.BaseFragment
import com.sun.doitpat.base.ViewModelFactory
import com.sun.doitpat.data.repository.ToDoRepository
import com.sun.doitpat.data.repository.impl.ToDoRepositoryImpl
import com.sun.doitpat.data.source.local.AppDatabase
import kotlinx.android.synthetic.main.fragment_main.*


class ToDoFragment : BaseFragment<ViewDataBinding, ToDoViewModel>() {

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

        recyclerView.adapter = toDoAdapter
        toDoViewModel.getList().observe(this, Observer {
            it?.let(toDoAdapter::submitList)
        })
    }

    private fun createViewModel() {
        toDoRepository?.let {
            toDoViewModel = ViewModelProviders.of(
                    this,
                    ViewModelFactory { ToDoViewModel(it) }).get(ToDoViewModel::class.java)
        }
    }
}
