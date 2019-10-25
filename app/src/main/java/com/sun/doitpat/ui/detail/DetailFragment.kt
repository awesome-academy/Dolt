package com.sun.doitpat.ui.detail

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.sun.doitpat.BR
import com.sun.doitpat.R
import com.sun.doitpat.base.BaseFragment
import com.sun.doitpat.base.ViewModelFactory
import com.sun.doitpat.data.repository.ToDoRepository
import com.sun.doitpat.data.repository.impl.ToDoRepositoryImpl
import com.sun.doitpat.data.source.local.AppDatabase
import com.sun.doitpat.databinding.FragmentDetailBinding
import kotlinx.android.synthetic.main.fragment_detail.*
import java.util.*

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEventsClick()
    }

    private fun createViewModel() {
        toDoRepository?.let {
            detailViewModel = ViewModelProviders.of(
                    this,
                    ViewModelFactory { DetailViewModel(it) }).get(DetailViewModel::class.java)
        }
    }

    private fun showTimeChooserDialog() {
        val currentCalendar = Calendar.getInstance()
        val startYear = currentCalendar.get(Calendar.YEAR)
        val startMonth = currentCalendar.get(Calendar.MONTH)
        val startDay = currentCalendar.get(Calendar.DAY_OF_MONTH)
        val startHour = currentCalendar.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentCalendar.get(Calendar.MINUTE)

        context?.let {
            DatePickerDialog(it, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                TimePickerDialog(it, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    val pickedTime = Calendar.getInstance()
                    pickedTime.set(year, month, dayOfMonth, hourOfDay, minute)
                    viewModel.addTime(pickedTime)
                }, startHour, startMinute, false).show()
            }, startYear, startMonth, startDay).show()
        }
    }

    private fun setEventsClick() {
        textSetTime.setOnClickListener {
            showTimeChooserDialog()
        }
        buttonSave.setOnClickListener {
            viewModel.add()
            Navigation.findNavController(it).popBackStack()
        }
        buttonCancel.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
    }

}
