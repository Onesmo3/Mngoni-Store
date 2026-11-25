package com.mngoni.store.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.mngoni.store.app.ui.components.PrimaryButton
import com.mngoni.store.app.ui.components.ProductThumb
import com.mngoni.store.app.ui.components.QuantityStepper
import com.mngoni.store.app.ui.components.SectionTitle
import com.mngoni.store.app.ui.components.SumRow
import com.mngoni.store.app.ui.viewmodels.CartViewModel

@Composable
fun CartScreen(
    vm: CartViewModel,
    onCheckout: () -> Unit,
    onBrowse: () -> Unit,
) {
    val lines by vm.lines.collectAsState()
    val subtotal by vm.subtotal.collectAsState()
    val delivery by vm.deliveryFee.collectAsState()
    val total by vm.total.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        SectionTitle("Cart yangu")
        Spacer(Modifier.height(12.dp))

        if (lines.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🧺", style = MaterialTheme.typography.displaySmall)
                    Spacer(Modifier.height(8.dp))
                    Text("Cart iko tupu.", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(12.dp))
                    PrimaryButton("Tafuta bidhaa", onBrowse, modifier = Modifier.fillMaxWidth())
                }
            }
            return@Column
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(lines, key = { it.product.id }) { line ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        ProductThumb(line.product, Modifier.size(56.dp).clip(RoundedCornerShape(12.dp)))
                        Spacer(Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(line.product.name, style = MaterialTheme.typography.titleSmall)
                            Text(line.subtotal.format(), color = MaterialTheme.colorScheme.primary)
                            QuantityStepper(
                                quantity = line.quantity,
                                onMinus = { vm.setQuantity(line.product.id, line.quantity - 1) },
                                onPlus = { vm.setQuantity(line.product.id, line.quantity + 1) },
                            )
                        }
                        TextButton(onClick = { vm.remove(line.product.id) }) { Text("Ondoa") }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                SumRow("Bidhaa", subtotal)
                Spacer(Modifier.height(6.dp))
                SumRow("Delivery", delivery)
                Spacer(Modifier.height(10.dp))
                SumRow("Jumla", total, emphasized = true)
            }
        }
        Spacer(Modifier.height(12.dp))
        PrimaryButton("Enda malipo", onCheckout)
    }
}
