package com.jovenulip.smovechallenge.map

import com.jovenulip.smovechallenge.R
import com.jovenulip.smovechallenge.data.api.ServiceApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.UnknownHostException

class MapsPresenter(private val mView: MapsContract.View) : MapsContract.Presenter {

    private val serviceApi by lazy {
        ServiceApi.create()
    }

    init {
        mView.presenter = this
    }

    override fun getCarLocations() {
        serviceApi.locations()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    mView.showCarLocations(result.data)
                }, { error ->
                    if (error is UnknownHostException) {
                        mView.showError(R.string.no_internet)
                    } else {
                        mView.showError(R.string.error_request)
                    }

                })
    }

}