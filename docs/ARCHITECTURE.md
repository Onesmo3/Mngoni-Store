# Mngoni Store — Architecture

## Kanuni za msingi

1. **UI haijui chanzo cha data.** Screens zinazungumza na `ViewModel`; ViewModels zinazungumza na repository interfaces zilizo kwenye `:core`.
2. **Domain ni pure Kotlin.** `:core` haina Android, hivyo inatestika haraka bila emulator.
3. **Hifadhi ni abstraction.** MVP inatumia DataStore + JSON; Firestore/Room wakuja baadaye bila kubadilisha UI.
4. **Secret haziko kwenye client.** Daraja credentials ziko kwenye Cloud Function / server, si kwenye APK.

## Modules

```
:app        Compose UI, Navigation, ViewModels, AppContainer (manual DI)
   |
   +--> :payments   MpesaClient, Retrofit gateway, phone normalization
   |
   +--> :data       SeedCatalogRepository, LocalStore, Cart/Order repositories
   |
   +--> :core       Money, Product, CartItem, Order, repository contracts
```

### `:core`

- `Money` — whole-shilling value object (`format()`, `+`, `*`, `compareTo`).
- `Category` / `Product` — catalog domain.
- `CartItem` / `CartLine` / `CartPricing` — pricing rules (subtotal, delivery fee, free-delivery threshold).
- `Order` / `OrderLine` / `OrderStatus` / `PaymentStatus`.
- Repository interfaces: `CatalogRepository`, `CartRepository`, `OrderRepository`.
- `MngoniJson` — shared `Json` configuration.

### `:data`

- `SeedCatalogRepository` — 12 products kwa kategoria zote; `observeProducts()` inarudisha `Flow`.
- `LocalStore` — contract ya `observeCart/saveCart/observeOrders/saveOrder`.
- `DataStoreLocalStore` — implementation kwa `DataStore<Preferences>` + kotlinx.serialization.
- `LocalCartRepository` — business rules za quantity (min 1, max 20).
- `LocalOrderRepository` — amri zinahifadhiwa local hadi Firebase ipo.

### `:payments`

- `PhoneNormalizer` — `0712...`, `+255712...`, `255712...` → `255712...`.
- `StkPushRequest` / `StkPushResponse` — DTO za gateway (si Daraja moja kwa moja).
- `MpesaApi` — `POST stkPush`.
- `DarajaMpesaClient` — Retrofit implementation; inarudisha `PaymentRequestResult` sealed type.

### `:app`

- `MngoniApp` — `Application` inayounda `AppContainer`.
- `AppContainer` — manual DI; badilisha na Hilt/Koin baadaye ukihitaji.
- `MngoniNavHost` — `home`, `catalog`, `cart`, `checkout`, `order_status/{id}`.
- `ui/` — design system (`MngoniTheme`, cards, buttons, price text).
- `screens/` — composables.
- `viewmodels/` — state holders.

## Data flow

### Kuangalia bidhaa

```
SeedCatalogRepository.observeProducts()
        -> CatalogViewModel.products: StateFlow<List<Product>>
        -> CatalogScreen (LazyVerticalGrid + category chips)
```

### Kuongeza kwenye cart

```
CatalogScreen -> CartViewModel.add(productId)
        -> LocalCartRepository.add() -> LocalStore.saveCart()
        -> CartViewModel.items: StateFlow<List<CartLine>>
        -> CartScreen (subtotal/delivery/total)
```

### Checkout + M-Pesa

```
CheckoutScreen
   -> CheckoutViewModel.placeOrder(name, phone)
       1. PhoneNormalizer.normalize(phone) -> null ? error UI
       2. CartPricing.total(lines)
       3. OrderRepository.placeOrder(PENDING, INITIATED)
       4. MpesaClient.requestStkPush(orderId, phone, amount)
          - success: save checkoutRequestId, clear cart, navigate order_status
          - failure: order inabaki PENDING + error, cart haifutwi
```

Mpaka callback ya Daraja itekelezwe kwenye server, status ya app ni "Imetuma prompt kwenye simu yako". Usiweke `PAID` kulingana na STK response pekee — `ResponseCode=0` inamaanisha *request ilikubaliwa*, si kwamba malipo yalikamilika.

## Firebase migration path

| Sasa | Baadaye |
|------|---------|
| `SeedCatalogRepository` | `FirestoreCatalogRepository` (collection `products`) |
| `DataStoreLocalStore` | Room au Firestore offline cache |
| `LocalOrderRepository` | `FirestoreOrderRepository` + Cloud Function callback |
| `AppContainer` | Hilt au Koin |

Kwa sababu UI inategemea interfaces tu, mabadiliko ni kwenye `:data` na `AppContainer` — hakuna screen inayobadilika.

## Testing

- `:core:test` — money formatting, pricing threshold, quantity rules.
- `:payments:test` — phone normalization.
- Firebase/Cloud Function tests za baadaye: emulator suite + fake Daraja responses.

## Threat model (malipo)

- APK ina endpoint ya HTTPS tu; haina secret yoyote ya Daraja.
- Gateway inapaswa kuthibitisha order amount kutoka kwenye database, si kutoka kwa request ya app pekee.
- Payload ya app inatumika kama "intent", sio kama ukweli wa malipo.
- Redirect/callback ya Daraja inathibitishwa kwenye server kwa `callbackUrl` au polling ya Daraja `stkquery`.
