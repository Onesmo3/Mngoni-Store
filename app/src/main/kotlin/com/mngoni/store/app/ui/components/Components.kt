package com.mngoni.store.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mngoni.store.core.Money
import com.mngoni.store.core.Product

@Composable
fun PriceText(
    amount: Money,
    style: TextStyle = MaterialTheme.typography.titleMedium,
) {
    Text(
        text = amount.format(),
        style = style,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.SemiBold,
    )
}

@Composable
fun ProductThumb(product: Product, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.secondaryContainer,
                    ),
                ),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(product.category.emoji, style = MaterialTheme.typography.headlineSmall)
    }
}

@Composable
fun ProductCard(product: Product, onAdd: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            ProductThumb(product, Modifier.size(62.dp))
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    product.category.label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(4.dp))
                PriceText(product.price, MaterialTheme.typography.titleSmall)
            }
            FilledTonalButton(onClick = onAdd) { Text("+") }
        }
    }
}

@Composable
fun QuantityStepper(
    quantity: Int,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onMinus, enabled = quantity > 1) { Text("−") }
        Text(
            quantity.toString(),
            modifier = Modifier.widthIn(min = 28.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
        )
        IconButton(onClick = onPlus) { Text("+") }
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(14.dp),
    ) {
        Text(text, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
}

@Composable
fun SumRow(label: String, amount: Money, emphasized: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
    ) {
        Text(label, style = if (emphasized) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium)
        PriceText(
            amount,
            if (emphasized) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
        )
    }
}
