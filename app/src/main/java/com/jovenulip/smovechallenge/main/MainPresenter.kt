package com.jovenulip.smovechallenge.main

import android.util.Log
import com.jovenulip.smovechallenge.data.api.ServiceApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainPresenter(private val mView: MainContract.View) : MainContract.Presenter {

    val serviceApi by lazy {
        ServiceApi.create()
    }

    init {
        mView.presenter = this
    }

    override fun getBookings(startTime : String, endTime : String) {
        serviceApi.availability(startTime, endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            mView.showBookings(result.data)
                        },

                        { error ->
                            Log.v("MainPresenter", error.message)
                        }
                )
    }

}