package com.jovenulip.smovechallenge.main

import com.jovenulip.smovechallenge.base.BasePresenter
import com.jovenulip.smovechallenge.base.BaseView
import com.jovenulip.smovechallenge.data.model.BookingModel

interface MainContract {

    interface View : BaseView<Presenter> {
        fun showBookings(mList: List<BookingModel.DataItems>)
    }

    interface Presenter : BasePresenter {
        fun getBookings(startTime : String, endTime : String)
    }
}