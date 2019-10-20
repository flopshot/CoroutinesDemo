package com.seannajera.coroutinesdemo.dagger

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Bound

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Provided