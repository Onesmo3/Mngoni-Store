package com.mngoni.store.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mngoni.store.app.ui.components.PrimaryButton
import com.mngoni.store.app.ui.components.SectionTitle
import com.mngoni.store.app.ui.components.SumRow
import com.mngoni.store.app.ui.viewmodels.CheckoutViewModel
import com.mngoni.store.core.Money

@Composable
fun CheckoutScreen(
    vm: CheckoutViewModel,
    total: Money,
    onDone: (orderId: String) -> Unit,
) {
    val state by vm.uiState.collectAsState()

    LaunchedEffect(state.placedOrderId) {
        state.placedOrderId?.let(onDone)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        SectionTitle("Malipo")
        Spacer(Modifier.height(6.dp))
        Text(
            "Tuma prompt ya M-Pesa kwenye simu yako. Thibitisha kwenye simu ili malipo yakamilike.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(18.dp))

        OutlinedTextField(
            value = state.name,
            onValueChange = vm::onNameChange,
            label = { Text("Jina la mteja") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = state.phone,
            onValueChange = vm::onPhoneChange,
            label = { Text("Namba ya M-Pesa") },
            placeholder = { Text("0712345678") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(18.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                SumRow("Jumla ya kulipa", total, emphasized = true)
            }
        }

        if (state.error != null) {
            Spacer(Modifier.height(14.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
            ) {
                Text(
                    state.error!!,
                    modifier = Modifier.padding(14.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        Spacer(Modifier.height(20.dp))
        PrimaryButton(
            text = if (state.isSubmitting) "Inatuma..." else "Lipia na M-Pesa",
            onClick = vm::placeOrder,
            enabled = !state.isSubmitting,
        )
    }
}
