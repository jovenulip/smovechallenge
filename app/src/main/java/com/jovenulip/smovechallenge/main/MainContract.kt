package com.jovenulip.smovechallenge.main

import com.jovenulip.smovechallenge.base.BasePresenter
import com.jovenulip.smovechallenge.base.BaseView

interface MainContract {

    interface View : BaseView<Presenter> {

    }

    interface Presenter : BasePresenter {
    }
}