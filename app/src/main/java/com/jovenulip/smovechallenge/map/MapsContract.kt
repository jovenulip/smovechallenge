package com.jovenulip.smovechallenge.map

import com.jovenulip.smovechallenge.base.BasePresenter
import com.jovenulip.smovechallenge.base.BaseView
import com.jovenulip.smovechallenge.data.model.CarsModel

interface MapsContract {

    interface View : BaseView<Presenter> {
        fun showCarLocations(mList: List<CarsModel.DataItems>)
    }

    interface Presenter : BasePresenter {
        fun getCarLocations()
    }
}