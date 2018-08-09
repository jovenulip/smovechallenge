package com.jovenulip.smovechallenge.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jovenulip.smovechallenge.R
import com.jovenulip.smovechallenge.data.model.BookingModel
import kotlinx.android.synthetic.main.item_booking.view.*

class BookingAdapter(val mContext: Context?, val mList: List<BookingModel.DataItems>, val mListener: (Int) -> Unit) :
        RecyclerView.Adapter<BookingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_booking, parent, false))
    }

    override fun onBindViewHolder(holder: BookingAdapter.ViewHolder, position: Int) {
        holder.bind(position, mListener)

        holder.txtCarId.text = mList[position].id.toString()
        holder.txtAvailableCars.text = mList[position].available_cars.toString()
        holder.txtDropOffLocations.text = mList[position].dropoff_locations.size.toString()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtCarId: TextView = view.txtCarId
        val txtAvailableCars: TextView = view.txtAvailableCars
        val txtDropOffLocations: TextView = view.txtDropOff

        fun bind(pos: Int, listener: (Int) -> Unit) = with(itemView) {
            itemView.setOnClickListener {
                listener(pos)
            }
        }

    }

}