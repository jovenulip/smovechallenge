package com.jovenulip.smovechallenge.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

object BookingModel {
    @Parcelize
    data class Data(val data: List<DataItems>) : Parcelable

    @Parcelize
    data class DataItems(val id: Int, val location: List<Double>, val available_cars: Int, val dropoff_locations: List<DropOffLocations>) : Parcelable

    @Parcelize
    data class DropOffLocations(val id: Int, val location: List<Double>) : Parcelable
}