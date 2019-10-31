package com.seannajera.coroutinesdemo

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.seannajera.coroutinesdemo.ui.MainActivity
import kotlinx.coroutines.runBlocking
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Test
    fun testMainActivity() = runBlocking {
        ActivityScenario.launch(MainActivity::class.java)

        // GIVEN we land on the home page
        onView(withId(R.id.text_home))
            .check(ViewAssertions.matches(withText("This is home Fragment")))

        // WHEN we navigate to the dashboard page
        onView(withId(R.id.navigation_dashboard))
            .perform(click())

        // THEN we expect to see an updated repo list from the view model after a repository sync
        onView(withId(R.id.repo_list)).checkWithTimeout {
            ViewAssertions.matches(atPosition(0, hasDescendant(withText("Item From Api"))))
        }
    }
}

fun atPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
    return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has item at position $position: ")
            itemMatcher.describeTo(description)
        }

        override fun matchesSafely(view: RecyclerView): Boolean {
            val viewHolder = view.findViewHolderForAdapterPosition(position)
                ?: // has no item on such position
                return false
            return itemMatcher.matches(viewHolder.itemView)
        }
    }
}
