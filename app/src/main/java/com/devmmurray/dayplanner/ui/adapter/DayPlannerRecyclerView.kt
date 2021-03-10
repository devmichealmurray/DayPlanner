package com.devmmurray.dayplanner.ui.adapter

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.devmmurray.dayplanner.BR
import com.devmmurray.dayplanner.data.model.local.*
import com.devmmurray.dayplanner.databinding.*
import com.devmmurray.dayplanner.ui.fragment.HomeFragmentDirections
import com.devmmurray.dayplanner.ui.fragment.NewsFragmentDirections
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

    fun bindArticles(article: NewsArticle) {
        binding.setVariable(BR.article, article)
        binding.root.setOnClickListener {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
            startActivity(binding.root.context, webIntent, null)
        }
        binding.executePendingBindings()
    }

    fun bindSearchSuggestions(suggestion: SuggestionObject) {
        binding.setVariable(BR.suggestions, suggestion)
        val directions = suggestion.suggestion?.let {
            NewsFragmentDirections
                .actionNavigationNewsToSearchResultsFragment(it)
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
        val newsBinding = NewsItemBinding.inflate(inflater, parent, false)
        val searchSuggestions = SearchSuggestionItemBinding.inflate(inflater, parent, false)

        return RVHolder(
            when (flag) {
                ListFlags.FORECASTS -> forecastsBinding
                ListFlags.TODO_TASK -> todoBinding
                ListFlags.EVENTS -> eventBinding
                ListFlags.NEWS_ARTICLE -> newsBinding
                ListFlags.SEARCH_SUGGESTION -> searchSuggestions
            }
        )
    }

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
        when (flag) {
            ListFlags.FORECASTS -> {
                holder.bindForecasts(list[position] as HourlyForecasts)
            }
            ListFlags.TODO_TASK -> {
                holder.bindTodoTasks(list[position] as TodoTask, TodoViewModel(Application()))
            }
            ListFlags.EVENTS -> {
                holder.bindEvents(list[position] as Event)
            }
            ListFlags.NEWS_ARTICLE -> {
                holder.bindArticles(list[position] as NewsArticle)
            }
            ListFlags.SEARCH_SUGGESTION -> {
                holder.bindSearchSuggestions(list[position] as SuggestionObject)
            }
        }
    }

    override fun getItemCount() = list.count()

}