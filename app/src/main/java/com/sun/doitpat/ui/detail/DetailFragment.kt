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
import com.sun.doitpat.util.Constants.COMPLETED
import com.sun.doitpat.util.Constants.DEFAULT_COLOR
import com.sun.doitpat.util.Constants.DEFAULT_ID
import com.sun.doitpat.util.Constants.EMPTY_STRING
import com.sun.doitpat.util.Constants.NEW
import com.sun.doitpat.util.Constants.NO_ALERT
import com.sun.doitpat.util.isLaterThanNow
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
            viewModel.reminderTime?.let { reminderTime ->
                if (it != EMPTY_STRING && reminderTime.isLaterThanNow())
                    showSwitch() else hideSwitch()
            }
        })
        viewModel.itemStatus.observe(viewLifecycleOwner, Observer {
            setCheckCompletedStatus(it)
        })
        setEventsClick()
    }

    private fun setCheckCompletedStatus(status: Int) {
        checkCompleted.isChecked = when (status) {
            NEW -> false
            else -> true
        }
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
        cardSetTime.setOnClickListener {
            showTimeChooserDialog()
        }
        cardClearTime.setOnClickListener {
            viewModel.clearTime()
        }
        checkReminder.setOnClickListener {
            if (checkReminder.isChecked) viewModel.setReminder(ALERT)
            else viewModel.setReminder(NO_ALERT)
        }
        cardSave.setOnClickListener {
            viewModel.addToDo()
            Navigation.findNavController(it).popBackStack()
        }
        cardCancel.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
        checkCompleted.setOnClickListener {
            if (checkCompleted.isChecked) viewModel.setCompletedStatus(COMPLETED)
            else viewModel.setCompletedStatus(NEW)
        }
    }

    private fun changeItemsColor(color: Int) {
        context?.let { context ->
            val backgroundId = when (color) {
                COLOR_RED -> R.drawable.fragment_background_red
                COLOR_ORANGE -> R.drawable.fragment_background_orange
                COLOR_YELLOW -> R.drawable.fragment_background_yellow
                COLOR_GREEN -> R.drawable.fragment_background_green
                COLOR_BLUE -> R.drawable.fragment_background_blue
                COLOR_PURPLE -> R.drawable.fragment_background_purple
                DEFAULT_COLOR -> R.drawable.fragment_background_gray
                else -> R.drawable.fragment_background_gray
            }
            val background = ContextCompat.getDrawable(context, backgroundId)
            background?.let { changeColorBackground(it) }
        }
    }

    private fun changeColorBackground(background: Drawable) {
        layoutButton.background = background
        layoutOption.background = background
    }

    private fun setSwitchStatus(alertStatus: Int) {
        checkReminder.isChecked = alertStatus != NO_ALERT
    }

    private fun showSwitch() {
        checkReminder.visibility = View.VISIBLE
    }

    private fun hideSwitch() {
        checkReminder.visibility = View.GONE
    }

}
