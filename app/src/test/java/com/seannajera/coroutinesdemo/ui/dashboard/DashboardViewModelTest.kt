package com.seannajera.coroutinesdemo.ui.dashboard

import com.nhaarman.mockitokotlin2.whenever
import com.seannajera.coroutinesdemo.persistence.Item
import com.seannajera.coroutinesdemo.repository.ItemRepository
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
        whenever(itemRepository.getItems()).thenReturn(flowOf(listOf(Item("ItemFromDB")), listOf(Item("ItemFromNetwork"))))

        // WHEN
        val itemTextFlow = dashboardViewModel.fetchText()

        // THEN
        val textItems = itemTextFlow.toList()
        val expectedItems = listOf("ItemFromDB", "ItemFromNetwork")
        assert(textItems == expectedItems) { "$textItems does not equal $expectedItems" }
    }
}