package com.jovenulip.smovechallenge.map

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.jovenulip.smovechallenge.AppHelper.Helper.getBitmapFromDrawable
import com.jovenulip.smovechallenge.Constants
import com.jovenulip.smovechallenge.R
import com.jovenulip.smovechallenge.data.model.BookingModel
import com.jovenulip.smovechallenge.data.model.CarsModel
import kotlinx.android.synthetic.main.activity_main.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, MapsContract.View {

    override lateinit var presenter: MapsContract.Presenter
    private lateinit var mMap: GoogleMap
    private lateinit var mClusterManager: ClusterManager<MapsClusterItem>
    private lateinit var bookingLocation: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = if (intent.action == Constants.ACTION_AVAILABLE_CARS)
            getString(R.string.drop_off_locations) else getString(R.string.car_location)

        presenter = MapsPresenter(this)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

    override fun showCarLocations(mList: List<CarsModel.DataItems>) {
        for (data in mList) {
            mClusterManager.addItem(MapsClusterItem(data.latitude, data.longitude, data.id.toString(), data.is_on_trip))
        }

        mClusterManager.cluster()
    }

    override fun showError(er: Int) {
        Snackbar.make(vwMain, getString(er), Snackbar.LENGTH_SHORT).show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val mapSettings = mMap.uiSettings
        mapSettings.isMapToolbarEnabled = false
        mapSettings.isCompassEnabled = false

        mClusterManager = ClusterManager(this, mMap)
        mClusterManager.renderer = CarClusterRenderer(this, mMap, mClusterManager)

        mMap.isIndoorEnabled = false
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager)

        if (intent.action == Constants.ACTION_AVAILABLE_CARS) {
            val mData: BookingModel.DataItems = intent.getParcelableExtra(Constants.AVAILABLE_CAR)
            val mLocationList: List<BookingModel.DropOffLocations> = mData.dropoff_locations

            for (data in mLocationList) {
                val locations: List<Double> = data.location
                mClusterManager.addItem(MapsClusterItem(locations[0], locations[1], data.id.toString(), true, true))
            }

            mClusterManager.cluster()
            bookingLocation = LatLng(mData.location[0], mData.location[1])

            mMap.addMarker(MarkerOptions().position(bookingLocation).icon(
                    BitmapDescriptorFactory.fromBitmap(getBitmapFromDrawable(getDrawable(R.drawable.ic_car_blue)))))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bookingLocation, 14F))

        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(Constants.SINGAPORE_LAT, Constants.SINGAPORE_LON), 12F))
            presenter.getCarLocations()
        }
    }

    class CarClusterRenderer(context: Context, map: GoogleMap, clusterManager: ClusterManager<MapsClusterItem>)
        : DefaultClusterRenderer<MapsClusterItem>(context, map, clusterManager) {

        private var bitmapCarBlue = getBitmapFromDrawable(context.getDrawable(R.drawable.ic_car_blue))
        private var bitmapCarGray = getBitmapFromDrawable(context.getDrawable(R.drawable.ic_car_gray))
        private var bitmapPlace = getBitmapFromDrawable(context.getDrawable(R.drawable.ic_place_red))
        private var strAvailable = context.getString(R.string.available)
        private var strNotAvailable = context.getString(R.string.not_available)
        private var strCarId = context.getString(R.string.car_id)
        private var strDropOffId = context.getString(R.string.drop_off_id)

        override fun onBeforeClusterItemRendered(item: MapsClusterItem, markerOptions: MarkerOptions) {
            super.onBeforeClusterItemRendered(item, markerOptions)

            if (item.isDropOffLocation()) {
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmapPlace))
                markerOptions.title("$strDropOffId ${item.title.toString()}")

            } else {

                if (item.isAvailable()) {
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmapCarBlue))
                } else {
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmapCarGray))
                }

                markerOptions.title("$strCarId ${item.title.toString()}")
                markerOptions.snippet(if (item.isAvailable()) strAvailable else strNotAvailable)
            }
        }

        override fun shouldRenderAsCluster(cluster: Cluster<MapsClusterItem>): Boolean {
            return cluster.size > 1
        }
    }
}
