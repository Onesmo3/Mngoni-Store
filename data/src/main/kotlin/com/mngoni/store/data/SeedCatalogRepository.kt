package com.mngoni.store.data

import com.mngoni.store.core.Category
import com.mngoni.store.core.Money
import com.mngoni.store.core.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/** Static catalog for MVP. Replace with FirestoreCatalogRepository later. */
object SeedCatalog {
    val products: List<Product> = listOf(
        Product("sh-01", "Sneaker Canvas Kijani", Category.SHOES, Money(18_000), "Viatu vya kila siku, soli imara.", 12),
        Product("sh-02", "Leather Formal Black", Category.SHOES, Money(42_000), "Viatu vya ofisi, ngozi halisi.", 6),
        Product("sh-03", "Kids School Shoe", Category.SHOES, Money(12_500), "Imara kwa shule, ukubwa 28-38.", 20),
        Product("sd-01", "Flip Flop Beach", Category.SANDALS, Money(6_500), "Sandals nyepesi za pwani.", 30),
        Product("sd-02", "Leather Sandal Brown", Category.SANDALS, Money(15_000), "Sandals za kiume, buckle imara.", 14),
        Product("sd-03", "Kids Slider", Category.SANDALS, Money(5_000), "Slider rahisi kwa watoto.", 25),
        Product("mc-01", "Shati Poplin White", Category.MEN_CLOTHING, Money(11_000), "Shati rasmi, matumizi ya ofisi.", 18),
        Product("mc-02", "T-Shirt Cotton Navy", Category.MEN_CLOTHING, Money(7_500), "Cotton 100%, comfortable.", 40),
        Product("mc-03", "Jeans Slim Fit", Category.MEN_CLOTHING, Money(22_000), "Denim imara, slim fit.", 9),
        Product("wc-01", "Kitenge Dress", Category.WOMEN_CLOTHING, Money(19_000), "Rangi za Kiafrika, ukubwa S-XL.", 15),
        Product("wc-02", "Blouse Silk Print", Category.WOMEN_CLOTHING, Money(16_500), "Blouse nyepesi, rangi mbalimbali.", 11),
        Product("ch-01", "Kids Unisex Tee", Category.CHILDREN_CLOTHING, Money(5_500), "T-shirt ya watoto, umri 2-12.", 35),
        Product("ch-02", "Girls Frock", Category.CHILDREN_CLOTHING, Money(9_000), "Frock ya sherehe, ukubwa 4-12.", 16),
        Product("jw-01", "Pendant Dhahabu", Category.JEWELLERY, Money(55_000), "Mnyororo + pendant, dhahabu 18k.", 4),
        Product("jw-02", "Bangle Set Silver", Category.JEWELLERY, Money(12_000), "Set ya bangili 4, silver finish.", 10),
    )
}

class SeedCatalogRepository : com.mngoni.store.core.CatalogRepository {
    override fun observeProducts(): Flow<List<Product>> = flowOf(SeedCatalog.products)

    override suspend fun refresh(): Result<Unit> = Result.success(Unit)

    override suspend fun findById(id: String): Product? = SeedCatalog.products.find { it.id == id }
}
