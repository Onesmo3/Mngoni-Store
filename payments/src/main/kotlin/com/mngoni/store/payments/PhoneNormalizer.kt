package com.mngoni.store.payments

/** Normalizes Tanzanian mobile numbers to MSISDN format expected by Daraja. */
object PhoneNormalizer {
    fun normalize(input: String): String? {
        val digits = input.filter { it.isDigit() }
        return when {
            digits.length == 9 && digits.startsWith('7') -> "255$digits"
            digits.length == 10 && digits.startsWith("07") -> "255${digits.removePrefix("0")}"
            digits.length == 12 && digits.startsWith("2557") -> digits
            else -> null
        }
    }
}
