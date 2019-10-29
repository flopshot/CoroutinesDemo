package com.seannajera.coroutinesdemo

import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import junit.framework.AssertionFailedError
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

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