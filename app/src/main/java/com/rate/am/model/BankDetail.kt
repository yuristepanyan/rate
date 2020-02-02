package com.rate.am.model

data class BankDetail(
    val id: String,
    val title: String,
    val address: String,
    val contact: String,
    val head: Byte,
    val location: LatLng,
    val workHours: List<Workhours>
)