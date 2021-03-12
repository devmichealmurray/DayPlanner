package com.devmmurray.dayplanner.ui.fragment

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.devmmurray.dayplanner.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class HomeFragmentTest {

    @Before
    fun setUp() {
        launchFragmentInContainer<HomeFragment>(
            fragmentArgs = null,
            initialState = Lifecycle.State.RESUMED,
            factory = null
        )
    }

    @Test
    fun test_isHomeFragmentInView() {
        onView(withId(R.id.home_scrollView)).check(matches(isDisplayed()))
    }



}