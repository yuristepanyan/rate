package com.rate.am.deserialization

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.rate.am.model.Currency
import com.rate.am.model.CurrencyContainer
import com.rate.am.model.Rate
import com.rate.am.model.RatesResponse
import java.lang.IllegalStateException
import java.lang.reflect.Type

private const val TITLE = "title"
private const val DATE = "date"
private const val LOGO = "logo"
private const val LIST = "list"
private const val BUY = "buy"
private const val SELL = "sell"
private const val CASH = "0"
private const val NO_CASH = "1"

class RatesResponseDeserializer : JsonDeserializer<RatesResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): RatesResponse {
        val jsonObj = json?.asJsonObject

        jsonObj?.let {
            val keys = it.keySet().toList()
            val rates = List(keys.size) { position ->
                val key = keys[position]
                getRateFromJson(key, it.getAsJsonObject(key))
            }
            return RatesResponse(rates)
        }

        return RatesResponse(listOf())
    }

    private fun getRateFromJson(id: String, json: JsonObject): Rate {
        val listObject = json.getAsJsonObject(LIST)
        val keys = listObject.keySet().toList()
        val currencies = List(keys.size) { position ->
            val key = keys[position]
            getCurrencyContainer(key, listObject.getAsJsonObject(key))
        }
        return Rate(
            id,
            json.get(TITLE).asString,
            json.get(LOGO).asString,
            json.get(DATE).asLong,
            currencies
        )
    }

    private fun getCurrencyContainer(name: String, json: JsonObject): CurrencyContainer {
            return CurrencyContainer(
                name,
                if(json.has(CASH)) getCurrency(json.getAsJsonObject(CASH)) else null,
                if(json.has(NO_CASH)) getCurrency(json.getAsJsonObject(NO_CASH)) else null
            )
    }

    private fun getCurrency(json: JsonObject): Currency {
        return Currency(json.get(BUY).asFloat, json.get(SELL).asFloat)
    }
}