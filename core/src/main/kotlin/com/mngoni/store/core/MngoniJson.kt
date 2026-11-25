package com.mngoni.store.core

import kotlinx.serialization.json.Json

object MngoniJson {
    val instance: Json = Json {
        ignoreUnknownKeys = true
        prettyPrint = false
        encodeDefaults = true
    }
}
