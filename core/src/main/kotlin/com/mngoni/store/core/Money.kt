package com.mngoni.store.core

import kotlinx.serialization.Serializable
import java.text.NumberFormat
import java.util.Locale

/** Whole Tanzanian shillings. TZS in practice has no minor unit. */
@Serializable
@JvmInline
value class Money(val shillings: Long) : Comparable<Money> {

    companion object {
        val ZERO = Money(0)
    }

    operator fun plus(other: Money): Money = Money(shillings + other.shillings)

    operator fun minus(other: Money): Money = Money(shillings - other.shillings)

    operator fun times(factor: Int): Money = Money(shillings * factor.toLong())

    override fun compareTo(other: Money): Int = shillings.compareTo(other.shillings)

    fun format(): String = "TSh ${NumberFormat.getNumberInstance(Locale.ENGLISH).format(shillings)}"
}
