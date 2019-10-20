package com.seannajera.coroutinesdemo.dagger

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.seannajera.coroutinesdemo.App
import com.seannajera.coroutinesdemo.dagger.components.DaggerAppComponent
import com.seannajera.coroutinesdemo.ui.MainActivity
import dagger.android.AndroidInjection
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection

object ComponentInjector {

    fun initInjection(app: App) {

        DaggerAppComponent.builder().application(app).build()
            .injectApplicationComponent(app)

        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                handleInjectable(activity)
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

    fun handleInjectable(activity: Activity) {
        if (activity is Injectable || activity is HasAndroidInjector) {
            AndroidInjection.inject(activity)

            if (activity is MainActivity) {
                activity.supportFragmentManager
                    .registerFragmentLifecycleCallbacks(
                        object : FragmentManager.FragmentLifecycleCallbacks() {
                            override fun onFragmentAttached(
                                fm: FragmentManager,
                                f: Fragment,
                                context: Context
                            ) {
                                if (f is Injectable || f is HasAndroidInjector) {
                                    AndroidSupportInjection.inject(f)
                                }
                            }
                        }, true
                    )
            }
        }
    }
}