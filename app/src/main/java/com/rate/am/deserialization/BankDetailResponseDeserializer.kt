package com.rate.am.deserialization

import com.google.gson.*
import com.rate.am.model.BankDetail
import com.rate.am.model.BankDetailResponse
import com.rate.am.model.LatLng
import com.rate.am.model.Workhours
import java.lang.reflect.Type

private const val LIST = "list"
private const val TITLE = "title"
private const val HEAD = "head"
private const val AM = "am"
private const val ADDRESS = "address"
private const val LOCATION = "location"
private const val LAT = "lat"
private const val LNG = "lng"
private const val CONTACT = "contacts"
private const val WORKHOURS = "workhours"
private const val DAYS = "days"
private const val HOURS = "hours"

class BankDetailResponseDeserializer : JsonDeserializer<BankDetailResponse> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): BankDetailResponse {
        val jsonObj = json?.asJsonObject

        jsonObj?.let {
            val listObjects = it.getAsJsonObject(LIST)
            val keys = listObjects.keySet().toList()
            val bankDetails = List(keys.size) { position ->
                val key = keys[position]
                getBankDetail(listObjects.getAsJsonObject(key), key)
            }
            return BankDetailResponse(bankDetails)
        }
        return BankDetailResponse(listOf())
    }

    private fun getBankDetail(obj: JsonObject, id: String): BankDetail {
        return BankDetail(
            id,
            obj.getAsJsonObject(TITLE).get(AM).asString,
            obj.getAsJsonObject(ADDRESS).get(AM).asString,
            obj.get(CONTACT).asString,
            obj.get(HEAD).asByte,
            getLatLng(obj.getAsJsonObject(LOCATION)),
            getWorkhours(obj.getAsJsonArray(WORKHOURS))
        )
    }

    private fun getLatLng(obj: JsonObject) =
        LatLng(obj.get(LAT).asDouble, obj.get(LNG).asDouble)

    private fun getWorkhours(jsonArray: JsonArray): List<Workhours> {
        return List(jsonArray.size()) { position ->
            val obj = jsonArray[position].asJsonObject
            Workhours(obj.get(DAYS).asString, obj.get(HOURS).asString)
        }
    }
}