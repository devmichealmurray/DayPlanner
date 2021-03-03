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
import com.devmmurray.dayplanner.R
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

        // Set Date Picker Time To Current System Time
        val date: Long = System.currentTimeMillis()
        addEventBinding.eventDatePicker.text =
            date.let { TimeStampProcessing.transformSystemTime(it, TimeFlags.FULL) }

        return addEventBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set Up Click to Navigate to Date Picker Fragment
        addEventBinding.eventDatePicker.setOnClickListener {
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

        // Cancel and Save button functions
        addEventBinding.cancelButton.setOnClickListener {
            Navigation.findNavController(addEventBinding.cancelButton)
                .navigate(R.id.action_addEventFragment_to_navigation_home)
        }

        addEventBinding.saveAction.setOnClickListener {
            val dateId = args.dateId
            val title = addEventBinding.eventTitle.text.toString().capitalize(Locale.ROOT)
            val date = args.datePickerTime
            val locationName = addEventBinding.eventLocationName.text.toString().capitalize(Locale.ROOT)
            val locationAddress = addEventBinding.eventLocationAddress.text.toString()
            val notes = addEventBinding.eventNotes.text.toString().capitalize(Locale.ROOT)

            addEventViewModel.prepareEvent(dateId, title, date, locationName, locationAddress, notes)

            Navigation.findNavController(addEventBinding.cancelButton)
                .navigate(R.id.action_addEventFragment_to_navigation_home)
        }
    }

    override fun onResume() {
        super.onResume()
        // Changing date displayed in UI to date chosen for event
        addEventBinding.eventDatePicker.text =
            args.datePickerTime.let { TimeStampProcessing.transformSystemTime(it, TimeFlags.FULL) }
        // Display any previously entered information about the event
        addEventBinding.eventTitle.setText(args.title)
        addEventBinding.eventLocationName.setText(args.location)
        addEventBinding.eventLocationAddress.setText(args.address)
        addEventBinding.eventNotes.setText(args.notes)
    }


    private fun hideKeyboard() {
        val imm =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

}


