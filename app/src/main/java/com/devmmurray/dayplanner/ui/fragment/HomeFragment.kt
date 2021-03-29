package com.devmmurray.dayplanner.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.devmmurray.dayplanner.R
import com.devmmurray.dayplanner.data.model.local.Event
import com.devmmurray.dayplanner.data.model.local.HourlyForecasts
import com.devmmurray.dayplanner.databinding.FragmentHomeBinding
import com.devmmurray.dayplanner.ui.adapter.DayPlannerRecyclerView
import com.devmmurray.dayplanner.ui.viewmodel.HomeViewModel
import com.devmmurray.dayplanner.util.flags.ListFlags
import com.google.android.material.transition.MaterialElevationScale
import org.jetbrains.anko.support.v4.alert

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var homeBinding: FragmentHomeBinding

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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        homeBinding.viewModel = homeViewModel
        homeBinding.home = this
        homeBinding.lifecycleOwner = this
        return homeBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeBinding.switchEventsToAll.setOnCheckedChangeListener { _, isChecked ->
            eventChangeListener(isChecked)
        }

        homeViewModel.apply {
            getWeatherFromDB()
            getEventsFromDB()
            getCityState()
            weatherProgress.observe(viewLifecycleOwner, weatherProgressObserver)
            forecastList.observe(viewLifecycleOwner, hourlyForecastObserver)
            eventsList.observe(viewLifecycleOwner, eventListObserver)
            eventProgress.observe(viewLifecycleOwner, eventProgressObserver)
            homeErrorMessage.observe(viewLifecycleOwner, errorObserver)
        }
    }


    /**
     *  Weather Observers
     */

    private val weatherProgressObserver = Observer<Boolean> {
        if (!it) {
            homeBinding.hourlyForecastRecycler.visibility = View.VISIBLE
            homeBinding.forecastProgressBar.visibility = View.INVISIBLE
        }
    }

    private val hourlyForecastObserver = Observer<List<HourlyForecasts>> { list ->
        if (list.isNotEmpty()) {
            homeBinding.hourlyForecastRecycler.adapter =
                DayPlannerRecyclerView(list, ListFlags.FORECASTS)
        }
    }


    /**
     *  Event Observers
     */

    private val eventListObserver = Observer<List<Event>> { list ->
        if (list.isNotEmpty()) {
            homeBinding.eventRecyclerProgressBar.visibility = View.INVISIBLE
            homeBinding.noEventsTextView.visibility = View.INVISIBLE
            homeBinding.todaysEventsRecycler.apply {
                visibility = View.VISIBLE
                adapter = DayPlannerRecyclerView(list, ListFlags.EVENTS)
            }
        } else {
            homeBinding.todaysEventsRecycler.visibility = View.INVISIBLE
            homeBinding.eventRecyclerProgressBar.visibility = View.INVISIBLE
            homeBinding.noEventsTextView.visibility = View.VISIBLE
        }
    }

    private val eventProgressObserver = Observer<Boolean> {
        if (it) homeBinding.eventRecyclerProgressBar.visibility = View.VISIBLE else View.INVISIBLE
    }

    private val errorObserver = Observer<String> { errorMessage ->
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
     * Click Listener Functionality
     */

    fun addEventNavigation() = Navigation.findNavController(homeBinding.addEventButton)
        .navigate(R.id.action_navigation_home_to_addEventFragment)


    fun moreButtonFunction() {
        homeBinding.apply {
            currentWeatherCard.visibility = View.VISIBLE
            more.visibility = View.INVISIBLE
            less.visibility = View.VISIBLE
        }
    }

    fun lessButtonFunction() {
        homeBinding.apply {
            currentWeatherCard.visibility = View.GONE
            less.visibility = View.INVISIBLE
            more.visibility = View.VISIBLE
        }
    }

    private fun eventChangeListener(isChecked: Boolean) {
        homeBinding.todaysEvents.text =
            if (isChecked) getString(R.string.all_events) else getString(R.string.today_s_events)
        homeViewModel.changeEventsList(isChecked)
    }

}