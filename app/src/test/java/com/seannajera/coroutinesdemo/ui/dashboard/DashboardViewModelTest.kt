package com.seannajera.coroutinesdemo.ui.dashboard

import com.nhaarman.mockitokotlin2.whenever
import com.seannajera.coroutinesdemo.persistence.Item
import com.seannajera.coroutinesdemo.repository.ItemRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Answers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DashboardViewModelTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private lateinit var itemRepository: ItemRepository
    @InjectMocks private lateinit var dashboardViewModel: DashboardViewModel

    @Test
    fun `GIVEN items from repo WHEN fetchText called VERIFY correct items returned`() = runBlocking {
        // GIVEN
        whenever(itemRepository.getItems()).thenReturn(flowOf(listOf(Item("ItemFromDB")), listOf(Item("ItemFromNetwork"))))

        // WHEN
        val itemTextFlow = dashboardViewModel.fetchText()

        // THEN
        val textItems = itemTextFlow.toList()
        assert(textItems[0] == "ItemFromDB")
        assert(textItems[1] == "ItemFromNetwork")
    }
}