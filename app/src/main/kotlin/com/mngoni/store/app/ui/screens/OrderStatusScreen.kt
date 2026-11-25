package com.mngoni.store.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mngoni.store.app.ui.components.PrimaryButton
import com.mngoni.store.app.ui.components.SectionTitle
import com.mngoni.store.app.ui.components.SumRow
import com.mngoni.store.app.ui.viewmodels.OrderStatusViewModel

@Composable
fun OrderStatusScreen(vm: OrderStatusViewModel, onDone: () -> Unit) {
    val order by vm.order.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        SectionTitle("Oda yako")
        Spacer(Modifier.height(14.dp))
        if (order == null) {
            Text("Inapakia oda...", style = MaterialTheme.typography.bodyMedium)
            return@Column
        }
        val current = order!!
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("✅ Prompt ya M-Pesa imetumwa", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                Text("Namba ya oda: ${current.id}", style = MaterialTheme.typography.bodyMedium)
                Text("Jina: ${current.customerName}", style = MaterialTheme.typography.bodyMedium)
                Text("Simu: ${current.phone}", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(16.dp))
                SumRow("Jumla", current.total, emphasized = true)
                Spacer(Modifier.height(16.dp))
                Text(
                    "Angalia simu yako na uidhinisha malipo. Hali ya oda itasasishwa baada ya Daraja kuthibitisha malipo.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        PrimaryButton("Rudi nyumbani", onDone)
    }
}
