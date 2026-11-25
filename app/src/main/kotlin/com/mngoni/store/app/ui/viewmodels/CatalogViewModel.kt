package com.mngoni.store.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mngoni.store.core.CatalogRepository
import com.mngoni.store.core.Category
import com.mngoni.store.core.Product
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogViewModel(private val catalog: CatalogRepository) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory.asStateFlow()

    val products: StateFlow<List<Product>> = combine(
        catalog.observeProducts(),
        _query,
        _selectedCategory,
    ) { all, rawQuery, category ->
        val q = rawQuery.trim()
        all.filter { product ->
            val matchesCategory = category == null || product.category == category
            val matchesQuery = q.isBlank() ||
                product.name.contains(q, ignoreCase = true) ||
                product.description.contains(q, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun onQueryChange(value: String) {
        _query.value = value
    }

    fun onCategorySelected(category: Category?) {
        _selectedCategory.value = category
    }

    fun refresh() {
        viewModelScope.launch { catalog.refresh() }
    }
}
