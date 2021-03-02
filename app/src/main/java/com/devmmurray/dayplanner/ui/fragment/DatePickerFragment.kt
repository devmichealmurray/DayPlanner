package com.devmmurray.dayplanner.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.devmmurray.dayplanner.databinding.FragmentDatePickerBinding
import com.devmmurray.dayplanner.util.time.TimeStampProcessing

private const val TAG = "DatePickerFragment"

class DatePickerFragment : Fragment() {

    private lateinit var datePickerBinding: FragmentDatePickerBinding
    private val args: DatePickerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        datePickerBinding = FragmentDatePickerBinding.inflate(inflater, container, false)
        return datePickerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        datePickerBinding.cancelButton.setOnClickListener {
            Navigation.findNavController(datePickerBinding.cancelButton)
                .popBackStack()
        }

        datePickerBinding.saveAction.setOnClickListener {
            val datePicker = datePickerBinding.datePicker1
            val timePicker = datePickerBinding.timePicker1
            val day = datePicker.dayOfMonth
            val month = datePicker.month + 1
            val year = datePicker.year
            val hour = timePicker.hour
            val minute = timePicker.minute

            val dateString = "$day-$month-$year $hour:$minute"
            val dateId = "$month-$day-$year"
            val dateInMillis = TimeStampProcessing.transformDateStringToMillis(dateString)

            val title = args.title
            val location = args.location
            val address = args.address
            val notes = args.notes
            val directions = DatePickerFragmentDirections
                .actionDatePickerFragmentToAddEventFragment(
                    title,
                    location,
                    address,
                    notes,
                    dateInMillis,
                    dateId)
            Navigation.findNavController(datePickerBinding.saveAction)
                .navigate(directions)
        }
    }





}