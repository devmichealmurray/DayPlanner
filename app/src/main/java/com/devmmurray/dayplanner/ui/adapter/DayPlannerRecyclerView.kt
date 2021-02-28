package com.devmmurray.dayplanner.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.devmmurray.dayplanner.BR
import com.devmmurray.dayplanner.data.model.local.HourlyForecasts
import com.devmmurray.dayplanner.data.model.local.TodoTask
import com.devmmurray.dayplanner.databinding.HourlyForecastItemBinding
import com.devmmurray.dayplanner.databinding.TodoItemBinding
import com.devmmurray.dayplanner.util.ListFlags

class RVHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindForecasts(weather: HourlyForecasts) {
        binding.setVariable(BR.forecastObject, weather)
        binding.executePendingBindings()
    }

    fun bindTodoTasks(task: TodoTask) {
        binding.setVariable(BR.taskObject, task)
        binding.executePendingBindings()
    }
}

class DayPlannerRecyclerView(private val list: List<Any>, private val flag: ListFlags,) :
    RecyclerView.Adapter<RVHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val inflater = LayoutInflater.from(parent.context)
        val forecastsBinding = HourlyForecastItemBinding.inflate(inflater, parent, false)
        val todoBinding = TodoItemBinding.inflate(inflater, parent, false)

        return RVHolder(
            when (flag) {
                ListFlags.FORECASTS -> forecastsBinding
                ListFlags.TODO -> todoBinding
            }
        )
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {

        when (flag) {
            ListFlags.FORECASTS -> {
                holder.bindForecasts(list[position] as HourlyForecasts)
            }
            ListFlags.TODO -> {
                holder.bindTodoTasks(list[position] as TodoTask)
            }
        }

    }

    override fun getItemCount() = list.count()

}