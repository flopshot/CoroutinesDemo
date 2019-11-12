package com.seannajera.coroutinesdemo.util

object Do { inline infix fun<reified T> exhaustive(any: T?) = any }