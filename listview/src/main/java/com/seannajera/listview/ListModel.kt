package com.seannajera.listview

interface ListModel {
    val id: String
    val layout: ListViewLayout
    fun contentSameAs(otherItem: Any): Boolean
}