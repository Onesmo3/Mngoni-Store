package com.mngoni.store.payments

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PhoneNormalizerTest {

    @Test
    fun `normalizes local and international formats`() {
        assertEquals("255712345678", PhoneNormalizer.normalize("0712345678"))
        assertEquals("255712345678", PhoneNormalizer.normalize("+255 712 345 678"))
        assertEquals("255712345678", PhoneNormalizer.normalize("255712345678"))
        assertEquals("255712345678", PhoneNormalizer.normalize("712345678"))
    }

    @Test
    fun `rejects invalid numbers`() {
        assertNull(PhoneNormalizer.normalize("123"))
        assertNull(PhoneNormalizer.normalize("0800000000"))
        assertNull(PhoneNormalizer.normalize("abc"))
    }
}
