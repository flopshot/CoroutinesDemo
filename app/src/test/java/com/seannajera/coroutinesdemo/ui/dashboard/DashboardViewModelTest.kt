package com.seannajera.coroutinesdemo.ui.dashboard

import com.nhaarman.mockitokotlin2.whenever
import com.seannajera.coroutinesdemo.persistence.Item
import com.seannajera.coroutinesdemo.repository.ItemRepository
import com.seannajera.coroutinesdemo.ui.components.ItemComponentModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DashboardViewModelTest {

    @Mock private lateinit var itemRepository: ItemRepository
    @InjectMocks private lateinit var dashboardViewModel: DashboardViewModel

    @Test
    fun `GIVEN items from repo WHEN fetchText called VERIFY correct items returned`() = runBlocking {
        // GIVEN
        whenever(itemRepository.getItems()).thenReturn(flowOf(listOf(Item("ItemFromDB")), listOf(Item("ItemFromDB"), Item("ItemFromNetwork"))))

        // WHEN
        val itemsFlow = dashboardViewModel.fetchText()

        // THEN
        val items = itemsFlow.toList()
        val expectedItems = listOf(arrayListOf(ItemComponentModel(Item("ItemFromDB"))), arrayListOf(ItemComponentModel(Item("ItemFromDB")), ItemComponentModel(Item("ItemFromNetwork"))))
        assert(items == expectedItems) { "$items does not equal $expectedItems" }
    }
}