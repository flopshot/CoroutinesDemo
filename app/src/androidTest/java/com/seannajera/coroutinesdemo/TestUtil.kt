package com.seannajera.coroutinesdemo

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.BoundedMatcher
import junit.framework.AssertionFailedError
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import org.hamcrest.Description
import org.hamcrest.Matcher

suspend fun ViewInteraction.checkWithTimeout(
    timeMillis: Long = 1000L,
    assertionLambda: () -> ViewAssertion
) {
    val viewInteraction = this
    try {
        withTimeout(timeMillis = timeMillis) {
            launch {
                while (isActive) {
                    try {
                        viewInteraction.check(assertionLambda.invoke())
                        break
                    } catch (e: AssertionFailedError) {
                        continue
                    }
                }
            }
        }
    } catch (e: TimeoutCancellationException) {
        throw AssertionFailedError("Could not match ViewInteraction: $viewInteraction")
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