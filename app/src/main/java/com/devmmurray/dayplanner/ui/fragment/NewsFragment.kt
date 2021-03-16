package com.devmmurray.dayplanner.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmmurray.dayplanner.R
import com.devmmurray.dayplanner.data.model.entity.NewsEntity
import com.devmmurray.dayplanner.data.model.local.SuggestionObject
import com.devmmurray.dayplanner.databinding.FragmentNewsBinding
import com.devmmurray.dayplanner.ui.adapter.DayPlannerRecyclerView
import com.devmmurray.dayplanner.ui.viewmodel.NewsViewModel
import com.devmmurray.dayplanner.util.flags.ListFlags
import com.devmmurray.dayplanner.util.Utils
import com.google.android.material.transition.MaterialElevationScale
import org.jetbrains.anko.support.v4.alert

class NewsFragment : Fragment() {
    private val newsViewModel: NewsViewModel by viewModels()
    private lateinit var newsBinding: FragmentNewsBinding

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
        newsBinding = FragmentNewsBinding.inflate(inflater, container, false)
        return newsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsBinding.setVariable(BR.newsFragment, this)

        newsViewModel.apply {
            getCurrentNews()
            getSuggestions()
            newsList.observe(viewLifecycleOwner, newsListObserver)
            suggestionList.observe(viewLifecycleOwner, suggestionsObserver)
            newsErrorMessage.observe(viewLifecycleOwner, newsErrorObserver)
        }
    }


    /**
     *  Observers
     */

    private val newsListObserver = Observer<List<NewsEntity>> { list ->
        val news = list.map { it.toNewsArticle() }
        newsBinding.newsRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = DayPlannerRecyclerView(news, ListFlags.NEWS_ARTICLE)
        }
    }

    private val suggestionsObserver = Observer<List<SuggestionObject>> { list ->
        newsBinding.searchSuggestionsRecycler.adapter =
            DayPlannerRecyclerView(list, ListFlags.SEARCH_SUGGESTION)
    }

    private val newsErrorObserver = Observer<String> { errorMessage ->
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
     *  Button Functionality -- Search Return Arrow
     */

    fun searchAction() {
        val searchTerm = newsBinding.searchEditText.text.toString()
        newsBinding.searchEditText.text.clear()
        newsBinding.searchEditText.clearFocus()
        context?.let { context ->
            view.let { view ->
                if (view != null) {
                    Utils.hideKeyboard(context, view)
                }
            }
        }
        val directions = NewsFragmentDirections
            .actionNavigationNewsToSearchResultsFragment(searchTerm)
        view?.let { Navigation.findNavController(it).navigate(directions) }
    }

}