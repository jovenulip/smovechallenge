package com.jovenulip.smovechallenge.map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MapsClusterItem() : ClusterItem {
    private var mPosition: LatLng? = null
    private var mTitle: String? = null
    private var mSnippet: String? = null
    private var isAvailable: Boolean = false
    private var isDropOffLocation: Boolean = false

    constructor(lat: Double, lon: Double, title : String, available: Boolean) : this() {
        mPosition = LatLng(lat, lon)
        mTitle = title
        isAvailable = available
    }

    constructor(lat: Double, lon: Double, title: String, available: Boolean, dropOffLocation : Boolean) : this() {
        mPosition = LatLng(lat, lon)
        mTitle = title
        isAvailable = available
        isDropOffLocation = dropOffLocation
    }

    override fun getSnippet(): String? {
        return mSnippet
    }

    override fun getTitle(): String? {
        return mTitle
    }

    override fun getPosition(): LatLng? {
        return mPosition
    }

    fun isAvailable(): Boolean{
        return isAvailable
    }

    fun isDropOffLocation(): Boolean{
        return isDropOffLocation
    }
}