package com.jovenulip.smovechallenge.main

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.jovenulip.smovechallenge.AppHelper.Helper.getCurrentDateToString
import com.jovenulip.smovechallenge.AppHelper.Helper.getCurrentTimeToString
import com.jovenulip.smovechallenge.AppHelper.Helper.getNextDayToString
import com.jovenulip.smovechallenge.AppHelper.Helper.getStringToDate
import com.jovenulip.smovechallenge.AppHelper.Helper.getStringToTime
import com.jovenulip.smovechallenge.AppHelper.Helper.getUnixTimeFromDate
import com.jovenulip.smovechallenge.Constants
import com.jovenulip.smovechallenge.R
import com.jovenulip.smovechallenge.data.model.BookingModel
import com.jovenulip.smovechallenge.map.MapsActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), MainContract.View {

    override lateinit var presenter: MainContract.Presenter
    private var isListShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this)

        txtStartDate.text = getCurrentDateToString(Date())
        txtEndDate.text = getNextDayToString(Date())
        txtStartTime.text = getCurrentTimeToString(Date())
        txtEndTime.text = getCurrentTimeToString(Date())

        txtStartDate.setOnClickListener { showDate(false) }
        txtEndDate.setOnClickListener { showDate(true) }
        txtStartTime.setOnClickListener { showTime(false) }
        txtEndTime.setOnClickListener { showTime(true) }

        btnShowAvailableCars.setOnClickListener {
            if (!isListShown) {
                val startDate = getUnixTimeFromDate("${txtStartDate.text} ${txtStartTime.text}")
                val endDate = getUnixTimeFromDate("${txtEndDate.text} ${txtEndTime.text}")

                progress.visibility = View.VISIBLE
                presenter.getBookings(startDate, endDate)
            } else {
                isListShown = false
                updateUI(isListShown)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_car, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_car -> {
                startActivity(Intent(this, MapsActivity::class.java))
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showBookings(mList: List<BookingModel.DataItems>) {
        isListShown = true
        updateUI(isListShown)
        progress.visibility = View.GONE

        rvBookingList.layoutManager = LinearLayoutManager(this)
        rvBookingList.adapter = BookingAdapter(this, mList) {
            val data = mList[it]

            val intent = Intent(this, MapsActivity::class.java)
            intent.action = Constants.ACTION_AVAILABLE_CARS
            intent.putExtra(Constants.AVAILABLE_CAR, data)

            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }
    }

    override fun showError(er : Int) {
        progress.visibility = View.GONE
        Snackbar.make(vwMain, getString(er), Snackbar.LENGTH_SHORT).show()
    }

    private fun showDate(isNextDay: Boolean) {
        val c = Calendar.getInstance()

        if (isNextDay) {
            c.add(Calendar.DAY_OF_MONTH, 1)
        }

        val yy = c.get(Calendar.YEAR)
        val mm = c.get(Calendar.MONTH)
        val dd = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val mDate = "${month + 1} $dayOfMonth $year"

            if (isNextDay) {
                txtEndDate.text = getStringToDate(mDate)
            } else {
                txtStartDate.text = getStringToDate(mDate)
            }

        }, yy, mm, dd)
        dpd.show()
    }

    private fun showTime(isNextDay: Boolean) {
        val c = Calendar.getInstance()

        val hr = c.get(Calendar.HOUR)
        val min = c.get(Calendar.MINUTE)

        val dpd = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->

            val time = when (hourOfDay < 12) {
                true -> getStringToTime("$hourOfDay $minute AM")
                false -> getStringToTime("${hourOfDay - 12} $minute PM")
            }

            if (isNextDay) {
                txtEndTime.text = time
            } else {
                txtStartTime.text = time
            }

        }, hr, min, false)
        dpd.show()
    }

    private fun updateUI(listShown: Boolean) {
        txtTitle.visibility = if (listShown) View.GONE else View.VISIBLE
        cardStart.visibility = if (listShown) View.GONE else View.VISIBLE
        cardEnd.visibility = if (listShown) View.GONE else View.VISIBLE
        rvBookingList.visibility = if (listShown) View.VISIBLE else View.GONE
        btnShowAvailableCars.text = if (listShown) getString(R.string.reset_booking) else getString(R.string.check_availability)
    }
}



