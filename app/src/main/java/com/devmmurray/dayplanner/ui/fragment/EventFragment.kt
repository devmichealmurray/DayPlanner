package com.devmmurray.dayplanner.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.devmmurray.dayplanner.BR
import com.devmmurray.dayplanner.R
import com.devmmurray.dayplanner.data.model.local.Event
import com.devmmurray.dayplanner.databinding.FragmentEventBinding
import com.devmmurray.dayplanner.ui.viewmodel.EventViewModel
import org.jetbrains.anko.support.v4.alert

class EventFragment : DialogFragment() {

    private val eventViewModel: EventViewModel by viewModels()
    private lateinit var eventBinding: FragmentEventBinding
    private val args: EventFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        eventBinding = FragmentEventBinding.inflate(inflater, container, false)
        return eventBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = args.eventId
        eventViewModel.apply {
            getEventById(id)
            eventErrorMessage.observe(viewLifecycleOwner, eventErrorObserver)
            returnEvent.observe(viewLifecycleOwner, { bindEvent(it) })
        }

        eventBinding.apply {
            deleteEvent.setOnClickListener { deleteEvent(id) }
            updateEvent.setOnClickListener { updateEvent(id) }
        }

    }

    private fun bindEvent(event: Event) {
        eventBinding.setVariable(BR.event, event)

        eventBinding.apply {
            directionsButton
                .setOnClickListener { event.address?.let { it1 -> mapsIntent(it1) } }
            shareEvent.setOnClickListener {
                shareEvent(
                    event.title, event.locationName, event.address, event.eventTime
                )
            }
        }
    }

    private fun mapsIntent(address: String) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address))
        val intent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }

    private fun shareEvent(title: String?, location: String?, address: String?, time: String?) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Don't Forget About This Event! \n$title \n$location \n$address \n$time"
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context?.startActivity(shareIntent)
    }

    private fun deleteEvent(id: Long) {
        eventViewModel.deleteEvent(id)
        dismiss()
    }

    private fun updateEvent(id: Long) {
        val directions = EventFragmentDirections.actionEventFragmentToAddEventFragment(id)
        parentFragment?.view?.let {
            Navigation.findNavController(it).navigate(directions)
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

}