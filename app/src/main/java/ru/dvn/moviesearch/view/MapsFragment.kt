package ru.dvn.moviesearch.view

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ru.dvn.moviesearch.R
import ru.dvn.moviesearch.model.staff.details.Place

private const val EXTRA_LAT = "EXTRA_LAT"
private const val EXTRA_LON = "EXTRA_LON"
private const val EXTRA_PLACE_NAME = "EXTRA_PLACE_NAME"

private const val DEFAULT_LAT = -34.0
private const val DEFAULT_LON = 151.0
private const val DEFAULT_PLACE_NAME = "Sydney"

class MapsFragment : Fragment() {
    private lateinit var map: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap

        arguments?.let { args->
            val lat = args.getDouble(EXTRA_LAT, DEFAULT_LAT)
            val lon = args.getDouble(EXTRA_LON, DEFAULT_LON)
            val name = args.getString(EXTRA_PLACE_NAME, DEFAULT_PLACE_NAME)

            moveMap(lat, lon, name)

        } ?: run {
            moveMap(DEFAULT_LAT, DEFAULT_LON, DEFAULT_PLACE_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


    private fun moveMap(lat:Double, lon: Double, name: String) {
        val place = LatLng(lat, lon)
        map.addMarker(MarkerOptions().position(place).title(name))
        map.moveCamera(CameraUpdateFactory.newLatLng(place))
    }

    companion object {
        fun newInstance(place: Place): MapsFragment {
            return MapsFragment().apply {
                arguments = Bundle().apply {
                    putDouble(EXTRA_LAT, place.lat)
                    putDouble(EXTRA_LON, place.lon)
                    putString(EXTRA_PLACE_NAME, place.name)
                }
            }
        }
    }
}