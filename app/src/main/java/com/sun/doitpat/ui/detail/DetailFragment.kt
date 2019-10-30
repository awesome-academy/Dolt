package com.sun.doitpat.ui.detail

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.sun.doitpat.BR
import androidx.lifecycle.Observer
import com.sun.doitpat.R
import com.sun.doitpat.base.BaseFragment
import com.sun.doitpat.base.ViewModelFactory
import com.sun.doitpat.data.repository.ToDoRepository
import com.sun.doitpat.data.repository.impl.ToDoRepositoryImpl
import com.sun.doitpat.data.source.local.AppDatabase
import com.sun.doitpat.databinding.FragmentDetailBinding
import com.sun.doitpat.util.Constants.ALERT
import com.sun.doitpat.util.Constants.COLOR_BLUE
import com.sun.doitpat.util.Constants.COLOR_GREEN
import com.sun.doitpat.util.Constants.COLOR_ORANGE
import com.sun.doitpat.util.Constants.COLOR_PURPLE
import com.sun.doitpat.util.Constants.COLOR_RED
import com.sun.doitpat.util.Constants.COLOR_YELLOW
import com.sun.doitpat.util.Constants.DEFAULT_COLOR
import com.sun.doitpat.util.Constants.DEFAULT_ID
import com.sun.doitpat.util.Constants.EMPTY_STRING
import com.sun.doitpat.util.Constants.NO_ALERT
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
    private var itemId: Int = DEFAULT_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createViewModel()
        arguments?.let { itemId = it.getInt(resources.getString(R.string.title_title)) }
        viewModel.getToDo(itemId)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.color.observe(viewLifecycleOwner, Observer {
            changeItemsColor(it)
        })
        viewModel.alertStatus.observe(viewLifecycleOwner, Observer {
            setSwitchStatus(it)
        })
        viewModel.time.observe(viewLifecycleOwner, Observer {
            viewModel.reminderTime.value?.let { reminderTime ->
                if (it != EMPTY_STRING && reminderTime >= 0) showSwitch() else hideSwitch()
            }
        })
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
                }, startHour, startMinute, true).show()
            }, startYear, startMonth, startDay).show()
        }
    }

    private fun setEventsClick() {
        textSetTime.setOnClickListener {
            showTimeChooserDialog()
        }
        textClearTime.setOnClickListener {
            viewModel.clearTime()
        }
        switchReminder.setOnClickListener {
            if (switchReminder.isChecked) viewModel.setReminder(ALERT)
            else viewModel.setReminder(NO_ALERT)
        }
        buttonSave.setOnClickListener {
            viewModel.addToDo()
            Navigation.findNavController(it).popBackStack()
        }
        buttonCancel.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
    }

    private fun changeItemsColor(color: Int) {
        context?.let { context ->
            val background = when (color) {
                COLOR_RED -> ContextCompat.getDrawable(context, R.drawable.fragment_background_red)
                COLOR_ORANGE -> ContextCompat.getDrawable(context, R.drawable.fragment_background_orange)
                COLOR_YELLOW -> ContextCompat.getDrawable(context, R.drawable.fragment_background_yellow)
                COLOR_GREEN -> ContextCompat.getDrawable(context, R.drawable.fragment_background_green)
                COLOR_BLUE -> ContextCompat.getDrawable(context, R.drawable.fragment_background_blue)
                COLOR_PURPLE -> ContextCompat.getDrawable(context, R.drawable.fragment_background_purple)
                DEFAULT_COLOR -> ContextCompat.getDrawable(context, R.drawable.fragment_background_gray)
                else -> ContextCompat.getDrawable(context, R.drawable.fragment_background_gray)
            }
            background?.let { changeColorBackground(it) }
        }
    }

    private fun changeColorBackground(background: Drawable) {
        layoutButton.background = background
        layoutOption.background = background
    }

    private fun setSwitchStatus(alertStatus: Int) {
        switchReminder.isChecked = alertStatus != NO_ALERT
    }

    private fun showSwitch() {
        switchReminder.visibility = View.VISIBLE
    }

    private fun hideSwitch() {
        switchReminder.visibility = View.GONE
    }

}
