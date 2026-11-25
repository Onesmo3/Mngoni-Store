package com.mngoni.store.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mngoni.store.app.ui.components.ProductCard
import com.mngoni.store.app.ui.components.SectionTitle
import com.mngoni.store.app.ui.viewmodels.CatalogViewModel
import com.mngoni.store.core.Category

@Composable
fun CatalogScreen(
    vm: CatalogViewModel,
    onAdd: (productId: String) -> Unit,
    onCart: () -> Unit,
) {
    val products by vm.products.collectAsState()
    val query by vm.query.collectAsState()
    val selected by vm.selectedCategory.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            SectionTitle("Bidhaa")
            Spacer(Modifier.weight(1f))
            TextButton(onClick = onCart) { Text("Cart →") }
        }
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(
            value = query,
            onValueChange = vm::onQueryChange,
            label = { Text("Tafuta bidhaa") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                FilterChip(
                    selected = selected == null,
                    onClick = { vm.onCategorySelected(null) },
                    label = { Text("Zote") },
                )
            }
            items(Category.entries) { category ->
                FilterChip(
                    selected = selected == category,
                    onClick = { vm.onCategorySelected(category) },
                    label = { Text("${category.emoji} ${category.label}") },
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        if (products.isEmpty()) {
            Text(
                "Hakuna bidhaa inayolingana na utafutaji wako.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                items(products, key = { it.id }) { product ->
                    ProductCard(product = product, onAdd = { onAdd(product.id) })
                }
            }
        }
    }
}
