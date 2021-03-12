package com.devmmurray.dayplanner.ui.activity

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.devmmurray.dayplanner.R
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class SplashActivityTest {

    @Test
    fun test_isSplashActivityInView() {
        val activityScenario = ActivityScenario.launch(SplashActivity::class.java)
        onView(withId(R.id.splash_activity)).check(matches(isDisplayed()))
    }

    @Test
    fun test_isLogoTextInView() {
        val activityScenario = ActivityScenario.launch(SplashActivity::class.java)
        onView(withId(R.id.splash_logo)).check(matches(isDisplayed()))
    }

    @Test
    fun test_isSplashQuoteInView() {
        val activityScenario = ActivityScenario.launch(SplashActivity::class.java)
        onView(withId(R.id.splash_quote)).check(matches(isDisplayed()))
    }

    @Test
    fun test_isProgressBarInView() {
        val activityScenario = ActivityScenario.launch(SplashActivity::class.java)
        onView(withId(R.id.splash_progress)).check(matches(isDisplayed()))
    }
}