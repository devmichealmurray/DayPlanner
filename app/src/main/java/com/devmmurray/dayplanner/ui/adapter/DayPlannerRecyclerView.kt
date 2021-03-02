package com.devmmurray.dayplanner.ui.adapter

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.devmmurray.dayplanner.BR
import com.devmmurray.dayplanner.data.model.local.Event
import com.devmmurray.dayplanner.data.model.local.HourlyForecasts
import com.devmmurray.dayplanner.data.model.local.TodoTask
import com.devmmurray.dayplanner.databinding.EventItemBinding
import com.devmmurray.dayplanner.databinding.HourlyForecastItemBinding
import com.devmmurray.dayplanner.databinding.TodoItemBinding
import com.devmmurray.dayplanner.ui.fragment.HomeFragmentDirections
import com.devmmurray.dayplanner.ui.viewmodel.TodoViewModel
import com.devmmurray.dayplanner.util.ListFlags

class RVHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindForecasts(weather: HourlyForecasts) {
        binding.setVariable(BR.forecastObject, weather)
        binding.executePendingBindings()
    }

    fun bindTodoTasks(task: TodoTask, viewModel: TodoViewModel) {
        binding.setVariable(BR.taskObject, task)
        binding.setVariable(BR.todoViewModel, viewModel)
        binding.executePendingBindings()
    }

    fun bindEvents(event: Event) {
        binding.setVariable(BR.event, event)
        val directions = event.id?.let {
            HomeFragmentDirections
                .actionNavigationHomeToEventFragment(it)
        }
        binding.root.setOnClickListener { view ->
            if (directions != null) {
                Navigation.findNavController(view)
                    .navigate(directions)
            }
        }
        binding.executePendingBindings()
    }
}

class DayPlannerRecyclerView(private val list: List<Any>, private val flag: ListFlags) :
    RecyclerView.Adapter<RVHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val inflater = LayoutInflater.from(parent.context)
        val forecastsBinding = HourlyForecastItemBinding.inflate(inflater, parent, false)
        val todoBinding = TodoItemBinding.inflate(inflater, parent, false)
        val eventBinding = EventItemBinding.inflate(inflater, parent, false)

        return RVHolder(
            when (flag) {
                ListFlags.FORECASTS -> forecastsBinding
                ListFlags.TODO -> todoBinding
                ListFlags.EVENTS -> eventBinding
            }
        )
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {

        when (flag) {
            ListFlags.FORECASTS -> {
                holder.bindForecasts(list[position] as HourlyForecasts)
            }
            ListFlags.TODO -> {
                holder.bindTodoTasks(list[position] as TodoTask, TodoViewModel(Application()))
            }
            ListFlags.EVENTS -> {
                holder.bindEvents(list[position] as Event)
            }
        }
    }

    override fun getItemCount() = list.count()

}