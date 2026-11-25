package com.mngoni.store.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mngoni.store.app.ui.components.PrimaryButton
import com.mngoni.store.app.ui.components.SectionTitle
import com.mngoni.store.core.Category

@Composable
fun HomeScreen(onBrowse: () -> Unit, onCart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary,
                        ),
                    ),
                )
                .padding(20.dp),
        ) {
            Column(modifier = Modifier.align(Alignment.BottomStart)) {
                Text(
                    "Mngoni Store",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    "Viatu, nguo na jewels kwa kila mtu.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                Spacer(Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    PrimaryButton(
                        text = "Angalia bidhaa",
                        onClick = onBrowse,
                        modifier = Modifier.weight(1f),
                    )
                    PrimaryButton(
                        text = "Cart",
                        onClick = onCart,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        SectionTitle("Kategoria")
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Category.entries.forEach { category ->
                AssistChip(
                    onClick = onBrowse,
                    label = { Text("${category.emoji} ${category.label}") },
                )
            }
        }

        Spacer(Modifier.height(24.dp))
        SectionTitle("Kwa nini Mngoni?")
        Spacer(Modifier.height(8.dp))
        Text(
            "• Bidhaa zilizochaguliwa kwa ubora.\n" +
                "• Malipo rahisi kwa M-Pesa.\n" +
                "• Delivery ndani ya Dar es Salaam na mikoa mingine.",
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
