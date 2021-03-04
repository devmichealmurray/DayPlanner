package com.devmmurray.dayplanner.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.devmmurray.dayplanner.BR
import com.devmmurray.dayplanner.R
import com.devmmurray.dayplanner.data.model.local.Event
import com.devmmurray.dayplanner.databinding.FragmentAddEventBinding
import com.devmmurray.dayplanner.ui.viewmodel.AddEventViewModel
import com.devmmurray.dayplanner.util.time.TimeFlags
import com.devmmurray.dayplanner.util.time.TimeStampProcessing
import java.util.*

private const val TAG = "AddEventFragment"

class AddEventFragment : Fragment() {

    private lateinit var addEventBinding: FragmentAddEventBinding
    private val addEventViewModel: AddEventViewModel by viewModels()
    private val args: AddEventFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addEventBinding = FragmentAddEventBinding.inflate(inflater, container, false)

        setDatePickerTime()
        getEvent(args.eventId)

        addEventViewModel.returnEvent.observe(viewLifecycleOwner, {
            val event = it.toEventObject()
            bindEvent(event)
        })

        return addEventBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //  Button ClickListeners
        addEventBinding.apply {
            eventDatePicker.setOnClickListener { navigateToDatePicker() }
            cancelButton.setOnClickListener { cancelButtonNavigation() }
            saveAction.setOnClickListener { saveActionNavigation() }
        }
    }

    override fun onResume() {
        super.onResume()
        bindOnResume()
    }

    private fun getEvent(id: Long) {
        if (id > 0L) addEventViewModel.getEventById(id)
    }


    private fun bindEvent(event: Event) {
        addEventBinding.setVariable(BR.event, event)
//        eventBinding.eventLocationAddress.setOnClickListener { event.address?.let { it1 -> mapsIntent(it1) } }
//        eventBinding.shareEvent.setOnClickListener {
//            shareEvent(
//                event.title, event.locationName, event.address, event.eventTime
//            ) }
//        addEventBinding.addEventTextView.text = "Event"
//        addEventBinding.saveAction.text = "Update"
    }

    private fun navigateToDatePicker() {
        hideKeyboard()

        val title = addEventBinding.eventTitle.text.toString()
        val location = addEventBinding.eventLocationName.text.toString()
        val address = addEventBinding.eventLocationAddress.text.toString()
        val notes = addEventBinding.eventNotes.text.toString()

        // Using safe args to persist any info that was previously entered
        val directions = AddEventFragmentDirections
            .actionAddEventFragmentToDatePickerFragment(
                title = title,
                location = location,
                address = address,
                notes = notes
            )
        Navigation.findNavController(addEventBinding.eventDatePicker)
            .navigate(directions)
    }

    private fun cancelButtonNavigation() {
        Navigation.findNavController(addEventBinding.cancelButton)
            .navigate(R.id.action_addEventFragment_to_navigation_home)
    }

    private fun saveActionNavigation() {
        val eventId = args.eventId
        val dateId = args.dateId
        val title = addEventBinding.eventTitle.text.toString().capitalize(Locale.ROOT)
        val date = args.datePickerTime
        val locationName =
            addEventBinding.eventLocationName.text.toString().capitalize(Locale.ROOT)
        val locationAddress = addEventBinding.eventLocationAddress.text.toString()
        val notes = addEventBinding.eventNotes.text.toString().capitalize(Locale.ROOT)

            addEventViewModel.prepareEvent(
                eventId, dateId, title, date, locationName, locationAddress, notes
            )

        Navigation.findNavController(addEventBinding.cancelButton)
            .navigate(R.id.action_addEventFragment_to_navigation_home)
    }

    private fun bindOnResume() {
        val event = Event(
            id = args.eventId,
            dateId = args.dateId,
            title = args.title,
            locationName = args.location,
            address = args.address,
            eventTime = args.datePickerTime.let {
                TimeStampProcessing.transformSystemTime(it, TimeFlags.FULL)
            },
            notes = args.notes
        )
        bindEvent(event)
    }

    private fun setDatePickerTime() {
        val date: Long = System.currentTimeMillis()
        addEventBinding.eventDatePicker.text =
            date.let { TimeStampProcessing.transformSystemTime(it, TimeFlags.FULL) }
    }

    private fun hideKeyboard() {
        val imm =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

}


