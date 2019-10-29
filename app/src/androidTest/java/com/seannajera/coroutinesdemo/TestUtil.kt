package com.seannajera.coroutinesdemo

import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import junit.framework.AssertionFailedError
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withTimeoutOrNull

suspend fun ViewInteraction.checkWithTimeout(
    timeMillis: Long = 1000L,
    assertionLambda: () -> ViewAssertion
) {
    val viewInteraction = this
    withTimeoutOrNull(timeMillis = timeMillis) {
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