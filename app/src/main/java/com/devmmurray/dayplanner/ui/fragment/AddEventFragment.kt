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
import com.devmmurray.dayplanner.data.model.local.Event
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
            eventSaved.observe(viewLifecycleOwner, eventSavedObserver)
            setDatePickerTime.observe(viewLifecycleOwner, setDatePickerTimeObserver)
        }
    }


    /**
     *  Observers
     */

    private val returnEventObserver = Observer<Event> { event ->
        addEventBinding.apply {
            addEventTextView.text = getString(R.string.update_event)
            saveAction.text = getString(R.string.update)
            eventTitle.setText(event.title)
            eventDatePicker.text = event.eventTime
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

    private val eventSavedObserver = Observer<Boolean> {
        if (it) Navigation.findNavController(addEventBinding.saveAction)
            .navigate(R.id.action_addEventFragment_to_navigation_home)
    }

    private val setDatePickerTimeObserver = Observer<String> {
        addEventBinding.eventDatePicker.text = it
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
        addEventViewModel.savedDay = dayOfMonth
        addEventViewModel.savedMonth = month + 1
        addEventViewModel.savedYear = year
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(context, this, hour, minute, false).show()
    }

    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, addEventViewModel.savedYear)
        calendar.set(Calendar.MONTH, addEventViewModel.savedMonth)
        calendar.set(Calendar.DAY_OF_MONTH, addEventViewModel.savedDay)
        calendar.set(Calendar.HOUR, hour)
        calendar.set(Calendar.MINUTE, minute)

        addEventViewModel.setNewTimeMillis(hour, minute)
    }


    /**
     * Button Functionality
     */

    fun cancelButtonNavigation() {
        Navigation.findNavController(addEventBinding.cancelButton)
            .navigate(R.id.action_addEventFragment_to_navigation_home)
    }

    fun saveButton() {
        addEventViewModel.prepareEvent(
            args.eventId,
            addEventBinding.eventTitle.text.toString().capitalize(Locale.ROOT),
            addEventBinding.eventLocationName.text.toString().capitalize(Locale.ROOT),
            addEventBinding.eventLocationAddress.text.toString(),
            addEventBinding.eventNotes.text.toString().capitalize(Locale.ROOT)
        )
    }

}


