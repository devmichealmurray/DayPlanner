package com.devmmurray.dayplanner.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.devmmurray.dayplanner.databinding.FragmentNewsBinding
import com.devmmurray.dayplanner.ui.viewmodel.NewsViewModel

class NewsFragment : Fragment() {

    private val newsViewModel: NewsViewModel by viewModels()
    private lateinit var newsBinding: FragmentNewsBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        newsBinding = FragmentNewsBinding.inflate(inflater, container, false)


        return newsBinding.root
    }
}