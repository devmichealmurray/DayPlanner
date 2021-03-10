package com.devmmurray.dayplanner.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmmurray.dayplanner.BR
import com.devmmurray.dayplanner.R
import com.devmmurray.dayplanner.data.model.entity.EventEntity
import com.devmmurray.dayplanner.data.model.entity.HourlyForecastEntity
import com.devmmurray.dayplanner.data.model.local.CityStateLocation
import com.devmmurray.dayplanner.data.model.local.CurrentWeather
import com.devmmurray.dayplanner.databinding.FragmentHomeBinding
import com.devmmurray.dayplanner.ui.adapter.DayPlannerRecyclerView
import com.devmmurray.dayplanner.ui.viewmodel.HomeViewModel
import com.devmmurray.dayplanner.util.ListFlags
import com.google.android.material.transition.MaterialElevationScale
import org.jetbrains.anko.support.v4.alert

private const val TAG = "Home Fragment"

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var homeBinding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        homeViewModel.apply {
            getWeatherFromDB()
            getEventsFromDB()
            getCityState()
            weatherProgress.observe(viewLifecycleOwner, weatherProgressObserver)
            eventProgress.observe(viewLifecycleOwner, eventProgressObserver)
            forecastList.observe(viewLifecycleOwner, weatherListObserver)
            eventsList.observe(viewLifecycleOwner, eventListObserver)
            homeErrorMessage.observe(viewLifecycleOwner, errorObserver)
            cityState.observe(viewLifecycleOwner, cityStateObserver)
            currentWeather.observe(viewLifecycleOwner, currentWeatherObserver)
            toastMessage.observe(viewLifecycleOwner, {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            })
        }

        homeBinding.apply {
            addEventButton.setOnClickListener { addEventNavigation() }
            more.setOnClickListener { moreButtonFunction() }
            less.setOnClickListener { lessButtonFunction() }
            switchEventsToAll.setOnCheckedChangeListener { _, isChecked ->
                homeBinding.todaysEvents.text = if (isChecked) "All Events" else "Todays Events"
                homeViewModel.changeEventsList(isChecked)
            }
        }
    }

    private val cityStateObserver = Observer<CityStateLocation> { location ->
        "${location.city}, ${location.state}".also { homeBinding.cityState.text = it }
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

    private val currentWeatherObserver = Observer<CurrentWeather> {
        homeBinding.setVariable(BR.currentWeather, it)
        homeBinding.setVariable(BR.weatherDescription, it.currentWeatherDescription)

    }


    /**
     *  Event Observers
     */

    private val eventListObserver = Observer<List<EventEntity>> { list ->
        val eventsList = list.map { it.toEventObject() }
        if (!eventsList.isNullOrEmpty()) {
            homeBinding.eventRecyclerProgressBar.visibility = View.INVISIBLE
            homeBinding.noEventsTextView.visibility = View.INVISIBLE
            homeBinding.todaysEventsRecycler.apply {
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter =
                    DayPlannerRecyclerView(eventsList, ListFlags.EVENTS)
            }
        } else {
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
     * Move to Binding
     */

    private fun addEventNavigation() {
        Navigation.findNavController(homeBinding.addEventButton)
            .navigate(R.id.action_navigation_home_to_addEventFragment)
    }

    private fun moreButtonFunction() {
        homeBinding.apply {
            currentWeatherCard.visibility = View.VISIBLE
            more.visibility = View.INVISIBLE
            less.visibility = View.VISIBLE
        }
    }

    private fun lessButtonFunction() {
        homeBinding.apply {
            currentWeatherCard.visibility = View.GONE
            less.visibility = View.INVISIBLE
            more.visibility = View.VISIBLE
        }
    }

}