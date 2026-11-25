package com.mngoni.store.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MoneyTest {

    @Test
    fun `formats shillings with grouping`() {
        assertEquals("TSh 12,500", Money(12_500).format())
        assertEquals("TSh 0", Money.ZERO.format())
    }

    @Test
    fun `supports arithmetic and comparison`() {
        val total = Money(1_000) + Money(2_500) + (Money(300) * 3)
        assertEquals(Money(4_400), total)
        assertTrue(total > Money(4_000))
    }
}
