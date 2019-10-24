package com.sun.doitpat.ui.detail

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.sun.doitpat.BR
import com.sun.doitpat.R
import com.sun.doitpat.base.BaseFragment
import com.sun.doitpat.base.ViewModelFactory
import com.sun.doitpat.data.repository.ToDoRepository
import com.sun.doitpat.data.repository.impl.ToDoRepositoryImpl
import com.sun.doitpat.data.source.local.AppDatabase
import com.sun.doitpat.databinding.FragmentDetailBinding

class DetailFragment : BaseFragment<FragmentDetailBinding, DetailViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_detail
    override val bindingVariable: Int
        get() = BR.item
    override val viewModel: DetailViewModel
        get() = detailViewModel

    private val toDoRepository by lazy {
        context?.let { ToDoRepositoryImpl(AppDatabase.invoke(it).toDoDao()) as ToDoRepository }
    }

    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createViewModel()
    }

    private fun createViewModel() {
        toDoRepository?.let {
            detailViewModel = ViewModelProviders.of(
                    this,
                    ViewModelFactory { DetailViewModel(it) }).get(DetailViewModel::class.java)
        }
    }
}
