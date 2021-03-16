package com.devmmurray.dayplanner.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devmmurray.dayplanner.R
import com.devmmurray.dayplanner.databinding.FragmentEventBinding
import com.devmmurray.dayplanner.ui.viewmodel.EventViewModel
import com.google.android.material.transition.MaterialElevationScale
import org.jetbrains.anko.support.v4.alert

class EventFragment : DialogFragment() {

    private val eventViewModel: EventViewModel by viewModels()
    private lateinit var eventBinding: FragmentEventBinding
    private val args: EventFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Motion Transitions
        enterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        eventBinding = FragmentEventBinding.inflate(inflater, container, false)
        eventBinding.viewModel = eventViewModel
        eventBinding.eventFragment = this
        eventBinding.lifecycleOwner = this
        return eventBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventViewModel.apply {
            getEventById(args.eventId)
            eventErrorMessage.observe(viewLifecycleOwner, eventErrorObserver)
            shareIntent.observe(viewLifecycleOwner, shareIntentObserver)
            mapsIntent.observe(viewLifecycleOwner, mapsIntentObserver)
        }
    }


    private val eventErrorObserver = Observer<String> { errorMessage ->
        alert {
            title = getString(R.string.error_alert_dialog)
            message = errorMessage
            isCancelable = false
            positiveButton(getString(R.string.error_alert_okay)) { dialog ->
                dialog.dismiss()
            }
        }.show()
    }

    private val shareIntentObserver = Observer<Intent> { shareIntent ->
        context?.startActivity(Intent.createChooser(shareIntent, null))
    }

    private val mapsIntentObserver = Observer<Intent> { mapsIntent ->
        startActivity(mapsIntent)
    }


    /**
     *  Button Functionality -- Delete, Update, Share, Directions
     */

    fun deleteEvent(id: Long) {
        context?.let { context ->
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.delete_event))
                .setMessage(getString(R.string.sure_to_delete))
                .setPositiveButton(getString(R.string.error_alert_okay)) { _, _ ->
                    okayToDelete(id)
                }
                .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }.create().show()
        }
    }

    private fun okayToDelete(id: Long) {
        eventViewModel.deleteEvent(id)
        dismiss()
    }

    fun updateEvent(id: Long) {
        findNavController().navigate(EventFragmentDirections
            .actionEventFragmentToAddEventFragment(id)
        )
    }

    fun shareEvent(title: String?, location: String?, address: String?, time: String?) {
        eventViewModel.createShareIntent(title, location, address, time)
    }

    fun eventDirections(address: String) {
        eventViewModel.createMapsIntent(address)
    }
}