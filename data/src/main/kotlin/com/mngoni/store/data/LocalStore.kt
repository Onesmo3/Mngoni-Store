package com.mngoni.store.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mngoni.store.core.CartItem
import com.mngoni.store.core.MngoniJson
import com.mngoni.store.core.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface LocalStore {
    fun observeCart(): Flow<List<CartItem>>
    suspend fun saveCart(items: List<CartItem>)
    fun observeOrders(): Flow<List<Order>>
    suspend fun saveOrder(order: Order)
}

class DataStoreLocalStore(private val dataStore: DataStore<Preferences>) : LocalStore {

    private val cartKey = stringPreferencesKey("cart.v1")
    private val ordersKey = stringPreferencesKey("orders.v1")
    private val json = MngoniJson.instance

    override fun observeCart(): Flow<List<CartItem>> = dataStore.data.map { prefs ->
        prefs[cartKey]?.let { json.decodeFromString<List<CartItem>>(it) } ?: emptyList()
    }

    override suspend fun saveCart(items: List<CartItem>) {
        dataStore.edit { prefs -> prefs[cartKey] = json.encodeToString(items) }
    }

    override fun observeOrders(): Flow<List<Order>> = dataStore.data.map { prefs ->
        prefs[ordersKey]?.let { json.decodeFromString<List<Order>>(it) } ?: emptyList()
    }

    override suspend fun saveOrder(order: Order) {
        dataStore.edit { prefs ->
            val current = prefs[ordersKey]?.let { json.decodeFromString<List<Order>>(it) } ?: emptyList()
            val updated = current.filterNot { it.id == order.id } + order
            prefs[ordersKey] = json.encodeToString(updated)
        }
    }
}
