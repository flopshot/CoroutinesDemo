package com.seannajera.coroutinesdemo

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.seannajera.coroutinesdemo.ui.MainActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Test
    fun testMainActivity() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.text_home))
            .check(ViewAssertions.matches(ViewMatchers.withText("This is home Fragment")))

        onView(withId(R.id.nav_view))
            .perform(click())
    }
}