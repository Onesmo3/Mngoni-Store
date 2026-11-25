# Mngoni Store

Android app ya e-commerce kwa viatu, sandals, nguo za kiume/kike/watoto, na jewels. Imetengenezwa kwa Kotlin + Jetpack Compose.

## Hali ya sasa (MVP scaffold)

- [x] Multi-module architecture: `core`, `data`, `payments`, `app`
- [x] Catalog yenye seed products (bila Firebase bado)
- [x] Cart + hifadhi ya ndani kwa DataStore/JSON
- [x] Checkout flow na M-Pesa STK Push client (gateway endpoint)
- [x] Username/phone validation na Phoenix/Daraja phone normalizer
- [x] Unit tests za money, cart pricing, na phone normalization
- [ ] Firebase (Auth + Firestore + Storage + Cloud Functions)
- [ ] Payment confirmation callback / polling
- [ ] Admin panel ya bidhaa
- [ ] Picha halisi za bidhaa + image caching

## Kujenga na kutest

```bash
./run-tests.sh      # core + payments JUnit
./build-apk.sh      # app-debug.apk
```

APK itatoka kwenye `app/build/outputs/apk/debug/app-debug.apk`.

## M-Pesa (Daraja) — muhimu

**Kamwe usiweke consumer key, consumer secret, au passkey kwenye app.** App inatuma tu:

```
POST {MPESA_BASE_URL}/stkPush
{
  "phone": "255712345678",
  "amount": 45000,
  "orderId": "ORD-...",
  "description": "Mngoni Store order ORD-..."
}
```

Cloud Function (au server yako) ndiyo inayoshikilia Daraja credentials na kuita `mpesa/stkpush/v1/processrequest`. Weka URL yake kwenye `local.properties`:

```properties
mpesa.baseUrl=https://us-central1-YOUR-PROJECT.cloudfunctions.net/api/
```

Kama haijawekwa, app inatuma `https://example.invalid/` — checkout itarudisha error wazi hadi config iwekwe.

## Firebase roadmap

Soma [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) kwa muundo kamili. Mpango ni:

1. `CatalogRepository` ibadilishwe hadi `FirestoreCatalogRepository` bila kubadilisha UI.
2. `LocalStore` ibaki kama cache ya offline.
3. Cloud Function ipokee callback ya Daraja, isashe `orders/{orderId}` kwenye Firestore.
4. App isikie `Order` kupitia `Flow` kutoka Firestore badala ya DataStore pekee.

## Package

- `com.mngoni.store.core` — domain models, money, pricing, repository contracts
- `com.mngoni.store.data` — seed catalog, DataStore persistence, repository implementations
- `com.mngoni.store.payments` — phone normalization, M-Pesa gateway client
- `com.mngoni.store.app` — Compose UI, navigation, ViewModels, manual DI container
