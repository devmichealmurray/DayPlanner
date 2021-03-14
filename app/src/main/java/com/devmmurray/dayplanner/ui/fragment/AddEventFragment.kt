package com.devmmurray.dayplanner.ui.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.devmmurray.dayplanner.R
import com.devmmurray.dayplanner.data.model.entity.EventEntity
import com.devmmurray.dayplanner.databinding.FragmentAddEventBinding
import com.devmmurray.dayplanner.ui.viewmodel.AddEventViewModel
import com.devmmurray.dayplanner.util.Utils
import com.devmmurray.dayplanner.util.time.TimeFlags
import com.devmmurray.dayplanner.util.time.TimeStampProcessing
import com.google.android.material.transition.MaterialElevationScale
import org.jetbrains.anko.support.v4.alert
import java.util.*


private const val TAG = "AddEventFragment"

class AddEventFragment : DialogFragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private lateinit var addEventBinding: FragmentAddEventBinding
    private val addEventViewModel: AddEventViewModel by viewModels()
    private val args: AddEventFragmentArgs by navArgs()

    var savedDay = -1
    var savedMonth = -1
    var savedYear = -1
    var savedMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Motion Transitions
        enterTransition = MaterialElevationScale(true).apply { duration = resources.getInteger(R.integer.motion_duration_large).toLong() }
        exitTransition = MaterialElevationScale(false).apply { duration = resources.getInteger(R.integer.motion_duration_large).toLong() }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Keeps EditText Fields Above the Keyboard
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        addEventBinding = FragmentAddEventBinding.inflate(inflater, container, false)

        return addEventBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addEventBinding.setVariable(BR.addEventFragment, this)
        addEventBinding.eventDatePicker.apply {
                text = TimeStampProcessing.todaysDate(TimeFlags.FULL)
                setOnClickListener { navigateToDatePicker() }
        }

        addEventViewModel.apply {
            eventChecker(args.eventId)
            returnEvent.observe(viewLifecycleOwner, returnEventObserver)
            addEventErrorMessage.observe(viewLifecycleOwner, errorMessageObserver)
        }
    }


    /**
     *  Observers
     */

    private val returnEventObserver = Observer<EventEntity> { event ->
        addEventBinding.apply {
            addEventTextView.text = getString(R.string.update_event)
            saveAction.text = getString(R.string.update)
            eventTitle.setText(event.title)
            eventDatePicker.text = event.eventTime?.let { eventTime ->
                TimeStampProcessing.transformSystemTime(
                    eventTime, TimeFlags.FULL
                )
            }
            event.eventTime?.let { savedMillis = it }
            eventLocationName.setText(event.locationName)
            eventLocationAddress.setText(event.address)
            eventNotes.setText(event.notes)
        }
    }

    private val errorMessageObserver = Observer<String> { errorMessage ->
        alert {
            title = getString(R.string.error_alert_dialog)
            message = errorMessage
            isCancelable = false
            positiveButton(getString(R.string.error_alert_okay)) { dialog ->
                dialog.dismiss()
            }
        }.show()
    }


    /**
     * Date & Time Picker Functionality
     */

    private fun navigateToDatePicker() {
        context?.let { context ->
            view.let { view ->
                if (view != null) {
                    Utils.hideKeyboard(context, view)
                }
            }
        }
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        context?.let { DatePickerDialog(it, this, year, month, day).show() }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month + 1
        savedYear = year
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(context, this, hour, minute, false).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, savedYear)
        calendar.set(Calendar.MONTH, savedMonth)
        calendar.set(Calendar.DAY_OF_MONTH, savedDay)
        calendar.set(Calendar.HOUR, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        val dateMillis = TimeStampProcessing
            .transformDateStringToMillis("$savedDay-$savedMonth-$savedYear $hourOfDay:$minute")
        savedMillis = dateMillis
        setDatePickerTime(dateMillis)
    }

    private fun setDatePickerTime(millis: Long) {
        addEventBinding.eventDatePicker.text =
            millis.let { TimeStampProcessing.transformSystemTime(it, TimeFlags.FULL) }
    }


    /**
     * Button Functionality
     */

    fun cancelButtonNavigation() {
        Navigation.findNavController(addEventBinding.cancelButton)
            .navigate(R.id.action_addEventFragment_to_navigation_home)
    }

    fun saveButton() {
        if (savedMillis > 0) {
            saveActionNavigation()
        } else {
            alert {
                title = getString(R.string.error_alert_dialog)
                message = "No Date or Time Selected For This Event"
                isCancelable = false
                positiveButton(getString(R.string.error_alert_okay)) { dialog ->
                    dialog.dismiss()
                }
            }.show()
        }
    }

    private fun saveActionNavigation() {
        val dateId = "$savedMonth-$savedDay-$savedYear"
        val title = addEventBinding.eventTitle.text.toString().capitalize(Locale.ROOT)
        val date = savedMillis
        val locationName =
            addEventBinding.eventLocationName.text.toString().capitalize(Locale.ROOT)
        val locationAddress = addEventBinding.eventLocationAddress.text.toString()
        val notes = addEventBinding.eventNotes.text.toString().capitalize(Locale.ROOT)

        addEventViewModel.prepareEvent(
            args.eventId, dateId, title, date, locationName, locationAddress, notes
        )

        Navigation.findNavController(addEventBinding.saveAction)
            .navigate(R.id.action_addEventFragment_to_navigation_home)
    }

}


