package com.seannajera.coroutinesdemo

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.seannajera.coroutinesdemo.ui.MainActivity
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Test
    fun testMainActivity() = runBlocking {
        ActivityScenario.launch(MainActivity::class.java)

        // GIVEN we land on the home page
        onView(withId(R.id.text_home))
            .check(ViewAssertions.matches(withText("Hello Mariana Tek!")))

        // WHEN we navigate to the dashboard page
        onView(withId(R.id.navigation_dashboard))
            .perform(click())

        // THEN we expect to see an updated repo list from the view model after a repository sync
        onView(withId(R.id.repo_list)).checkWithTimeout {
            ViewAssertions.matches(atPosition(0, hasDescendant(withText("Item From Api"))))
        }
    }
}
