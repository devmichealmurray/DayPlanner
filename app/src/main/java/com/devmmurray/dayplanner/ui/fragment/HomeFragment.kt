package com.devmmurray.dayplanner.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmmurray.dayplanner.R
import com.devmmurray.dayplanner.data.model.entity.HourlyForecastEntity
import com.devmmurray.dayplanner.data.model.local.Event
import com.devmmurray.dayplanner.databinding.FragmentHomeBinding
import com.devmmurray.dayplanner.ui.adapter.DayPlannerRecyclerView
import com.devmmurray.dayplanner.ui.viewmodel.HomeViewModel
import com.devmmurray.dayplanner.util.ListFlags
import org.jetbrains.anko.support.v4.alert

private const val TAG = "Home Fragment"

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var homeBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)

        homeViewModel.apply {
            getWeatherFromDB()
            getEventsFromDB()
            weatherProgress.observe(viewLifecycleOwner, weatherProgressObserver)
            eventProgress.observe(viewLifecycleOwner, eventProgressObserver)
            forecastList.observe(viewLifecycleOwner, weatherListObserver)
            eventsList.observe(viewLifecycleOwner, eventListObserver)
            homeErrorMessage.observe(viewLifecycleOwner, errorObserver)
        }

        homeBinding.addEvent.setOnClickListener {
            Navigation.findNavController(homeBinding.addEvent)
                .navigate(R.id.action_navigation_home_to_addEventFragment)
        }
        return homeBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * Huh??? observing twice??
         */
        homeViewModel.forecastList.observe(viewLifecycleOwner, weatherListObserver)
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


    private val weatherListObserver = Observer<List<HourlyForecastEntity>> { list ->
        val forecastList = list.map { it.toHourlyForecastObject() }
        if (forecastList.isNotEmpty()) {
            homeBinding.hourlyForecastRecycler.apply {
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = DayPlannerRecyclerView(forecastList, ListFlags.FORECASTS)
            }
        }
    }

    /**
     *  Event Observers
     */


    private val eventProgressObserver = Observer<Boolean> {
        if (it) homeBinding.eventRecyclerProgressBar.visibility = View.VISIBLE else View.INVISIBLE
    }

    private val eventListObserver = Observer<List<Event>> { eventList ->
        if (eventList.isEmpty()) {
            homeBinding.noEventsTextView.visibility = View.VISIBLE
        } else {
            homeBinding.noEventsTextView.visibility = View.INVISIBLE
            homeBinding.todaysEventsRecycler.apply {
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = DayPlannerRecyclerView(eventList, ListFlags.EVENTS)
            }
            homeBinding.todaysEventsRecycler.visibility = View.VISIBLE
        }
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



}