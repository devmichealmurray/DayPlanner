package com.devmmurray.dayplanner.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.devmmurray.dayplanner.R
import com.devmmurray.dayplanner.databinding.FragmentAddEventBinding
import com.devmmurray.dayplanner.ui.viewmodel.AddEventViewModel
import com.devmmurray.dayplanner.util.time.TimeFlags
import com.devmmurray.dayplanner.util.time.TimeStampProcessing
import org.jetbrains.anko.sdk27.coroutines.onClick

class AddEventFragment : Fragment() {

    private lateinit var addEventBinding: FragmentAddEventBinding
    private val addEventViewModel: AddEventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addEventBinding = FragmentAddEventBinding.inflate(inflater, container, false)
        return addEventBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val date: Long = System.currentTimeMillis()
        addEventBinding.eventDatePicker.text =
            date.let { TimeStampProcessing.transformSystemTime(it, TimeFlags.FULL) }

        addEventBinding.eventDatePicker.onClick {
            Navigation.findNavController(addEventBinding.eventDatePicker)
                .navigate(R.id.action_addEventFragment_to_datePickerFragment)
        }

        addEventBinding.cancelButton.onClick {
            Navigation.findNavController(addEventBinding.cancelButton).popBackStack()
        }

        addEventBinding.saveAction.onClick {
            val title = addEventBinding.eventTitle.text.toString()
            val locationName = addEventBinding.eventLocationName.text.toString()
            val locationAddress = addEventBinding.eventLocationAddress.text.toString()
            val notes = addEventBinding.eventNotes.text.toString()
            addEventViewModel.prepareEvent(title, locationName, locationAddress, notes)
        }

        addEventViewModel.preparedDate.observe(viewLifecycleOwner, {
            addEventBinding.eventDatePicker.text = it
        })
    }


}


