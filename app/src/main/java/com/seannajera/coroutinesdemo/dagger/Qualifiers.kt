package com.seannajera.coroutinesdemo.dagger

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ToDagger

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class FromDagger