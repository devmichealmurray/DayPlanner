package com.devmmurray.dayplanner.ui.activity

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.devmmurray.dayplanner.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @Before
    fun setUp() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun test_isActivityInView() {
        onView(withId(R.id.main_activity)).check(matches(isDisplayed()))
    }

    @Test
    fun test_isHomeFragmentInView() {
        onView(withId(R.id.home_scrollView)).check(matches(isDisplayed()))
    }

    @Test
    fun test_isNavBarInView() {
        onView(withId(R.id.nav_view)).check(matches(isDisplayed()))
    }

    @Test
    fun test_bottomNavBarNavigation() {
        onView(withId(R.id.navigation_todo)).perform(click())
        onView(withId(R.id.todo_fragment_container)).check(matches(isDisplayed()))

        onView(withId(R.id.navigation_news)).perform(click())
        onView(withId(R.id.news_fragment_container)).check(matches(isDisplayed()))

        onView(withId(R.id.navigation_home)).perform(click())
        onView(withId(R.id.home_scrollView)).check(matches(isDisplayed()))
    }

    @Test
    fun test_homeFragmentUI() {
        onView(withId(R.id.todays_outlook)).check(matches(isDisplayed()))
        onView(withId(R.id.city_state)).check(matches(isDisplayed()))
        onView(withId(R.id.hourly_forecast_recycler)).check(matches(isDisplayed()))
        onView(withId(R.id.more)).check(matches(isDisplayed()))
        onView(withId(R.id.todays_events)).check(matches(isDisplayed()))
        onView(withId(R.id.switch_events_to_all)).check(matches(isDisplayed()))
        onView(withId(R.id.all_events_textView)).check(matches(isDisplayed()))
        onView(withId(R.id.add_event_button)).check(matches(isDisplayed()))
        onView(withId(R.id.add_event_button)).perform(click())
        onView(withId(R.id.add_fragment_container)).check(matches(isDisplayed()))
    }

    @Test
    fun test_todoFragmentUI() {
        onView(withId(R.id.navigation_todo)).perform(click())
        onView(withId(R.id.todo_fragment_container)).check(matches(isDisplayed()))
        onView(withId(R.id.todo_header_text)).check(matches(isDisplayed()))
        onView(withId(R.id.add_task_edittext)).check(matches(isDisplayed()))
        onView(withId(R.id.return_button)).check(matches(isDisplayed()))

    }

    @Test
    fun test_newsFragmentUI() {
        onView(withId(R.id.navigation_news)).perform(click())
        onView(withId(R.id.news_fragment_container)).check(matches(isDisplayed()))
        onView(withId(R.id.news_header)).check(matches(isDisplayed()))
        onView(withId(R.id.search_editText)).check(matches(isDisplayed()))
        onView(withId(R.id.return_button)).check(matches(isDisplayed()))
        onView(withId(R.id.search_suggestions_recycler)).check(matches(isDisplayed()))
    }
}