package com.devmmurray.dayplanner.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.devmmurray.dayplanner.databinding.FragmentDatePickerBinding
import com.devmmurray.dayplanner.ui.viewmodel.AddEventViewModel
import org.jetbrains.anko.sdk27.coroutines.onClick

class DatePickerFragment : Fragment() {

    private lateinit var datePickerBinding: FragmentDatePickerBinding
    private val addEventViewModel: AddEventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        datePickerBinding = FragmentDatePickerBinding.inflate(inflater, container, false)
        return datePickerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        datePickerBinding.cancelButton.onClick {
            Navigation.findNavController(datePickerBinding.cancelButton)
                .popBackStack()
        }

        datePickerBinding.saveAction.onClick {
            val datePicker = datePickerBinding.datePicker1
            val day = datePicker.dayOfMonth
            val month = datePicker.month
            val year = datePicker.year

            addEventViewModel.prepareDate(day, month, year)
        }
    }


}