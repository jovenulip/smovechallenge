package com.jovenulip.smovechallenge.base

interface BaseView<T> {
    var presenter: T
    fun showError()
}
