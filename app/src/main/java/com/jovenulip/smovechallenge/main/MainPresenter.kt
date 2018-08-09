package com.jovenulip.smovechallenge.main

import com.jovenulip.smovechallenge.R
import com.jovenulip.smovechallenge.data.api.ServiceApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.UnknownHostException

class MainPresenter(private val mView: MainContract.View) : MainContract.Presenter {

    private val serviceApi by lazy {
        ServiceApi.create()
    }

    init {
        mView.presenter = this
    }

    override fun getBookings(startTime: String, endTime: String) {
        serviceApi.availability(startTime, endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            mView.showBookings(result.data)
                        },

                        { error ->

                            if (error is UnknownHostException) {
                                mView.showError(R.string.no_internet)
                            } else {
                                mView.showError(R.string.error_request)
                            }
                        }
                )
    }

}