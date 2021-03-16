package com.devmmurray.dayplanner.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devmmurray.dayplanner.R
import com.devmmurray.dayplanner.data.model.local.NewsArticle
import com.devmmurray.dayplanner.databinding.FragmentSearchResultsBinding
import com.devmmurray.dayplanner.ui.adapter.DayPlannerRecyclerView
import com.devmmurray.dayplanner.ui.viewmodel.SearchResultsViewModel
import com.devmmurray.dayplanner.util.flags.ListFlags
import org.jetbrains.anko.support.v4.alert

class SearchResultsFragment : Fragment() {

    private val searchResultsViewModel: SearchResultsViewModel by viewModels()
    private lateinit var searchResultsBinding: FragmentSearchResultsBinding
    private val args: SearchResultsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchResultsBinding = FragmentSearchResultsBinding.inflate(inflater, container, false)
        searchResultsBinding.fragment = this
        return searchResultsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchResultsViewModel.apply {
            deleteOldNews()
            getSearchResults(args.searchTerm)
            getNewsArticles()
            searchErrorMessage.observe(viewLifecycleOwner, searchErrorObserver)
            searchResultList.observe(viewLifecycleOwner, searchResultObserver)
        }
    }

    private val searchErrorObserver = Observer<String> { errorMessage ->
        alert {
            title = getString(R.string.error_alert_dialog)
            message = errorMessage
            isCancelable = false
            positiveButton(getString(R.string.error_alert_okay)) { dialog ->
                dialog.dismiss()
            }
        }.show()
    }

    private val searchResultObserver = Observer<List<NewsArticle>> { newsSearchResults ->
        searchResultsBinding.newsSearchRecycler.adapter =
            DayPlannerRecyclerView(newsSearchResults, ListFlags.NEWS_ARTICLE)
    }

    fun onBackPressed() {
        findNavController().popBackStack()
    }

}