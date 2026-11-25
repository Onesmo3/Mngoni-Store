package com.mngoni.store.app

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.mngoni.store.app.BuildConfig.MPESA_BASE_URL
import com.mngoni.store.core.CartRepository
import com.mngoni.store.core.CatalogRepository
import com.mngoni.store.core.OrderRepository
import com.mngoni.store.data.DataStoreLocalStore
import com.mngoni.store.data.LocalCartRepository
import com.mngoni.store.data.LocalOrderRepository
import com.mngoni.store.data.SeedCatalogRepository
import com.mngoni.store.payments.DarajaMpesaClient
import com.mngoni.store.payments.MpesaClient
import java.io.File

/** Manual DI container. Swap implementations here when Firebase is introduced. */
class AppContainer(context: Context) {

    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile = { File(context.applicationContext.filesDir, "mngoni-store.preferences_pb") },
    )

    private val localStore = DataStoreLocalStore(dataStore)

    val catalogRepository: CatalogRepository = SeedCatalogRepository()
    val cartRepository: CartRepository = LocalCartRepository(localStore)
    val orderRepository: OrderRepository = LocalOrderRepository(localStore)
    val mpesaClient: MpesaClient = DarajaMpesaClient(MPESA_BASE_URL)
}
