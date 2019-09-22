package com.example.family_artifact_register.UI.MapServiceFragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.family_artifact_register.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the
 * {@link MapDisplayFragment.OnFragmentInteractionListener} interface to handle interaction events.
 * Use the {@link MapDisplayFragment#newInstance} factory method to create an instance of this
 * fragment.
 */
public class MapDisplayFragment extends BasePlacesFragment implements OnMapReadyCallback {
    /**
     * class tag
     */
    public static final String TAG = MapDisplayFragment.class.getSimpleName();
    protected static final String LOCATIONS = "locations";
    // Stores the map object to be operated
    protected GoogleMap mMap;
    // Stores the locations to be displayed on screen
    protected List<MyLocation> locations = new ArrayList<>();
    // MapView the current fragment is operating on
    protected MapView mapView;

    // TODO to be implemented in the future for a correct way for interaction
    private OnFragmentInteractionListener mListener;

    /**
     * Required empty public constructor
     */
    public MapDisplayFragment() { }

    /**
     * Use this factory method to create a new instance of this fragment using the provided
     * parameters.
     * @param locations The locations to be displayed on the google map
     *
     * @return A new instance of fragment MapDisplayFragment.
     */
    public static MapDisplayFragment newInstance(List<MyLocation> locations) {
        MapDisplayFragment fragment = new MapDisplayFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(LOCATIONS, (Serializable) locations);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of this fragment using the provided
     * parameters.
     *
     * @return A new instance of fragment MapDisplayFragment.
     */
    public static MapDisplayFragment newInstance() {
        MapDisplayFragment fragment = new MapDisplayFragment();
        Bundle bundle = new Bundle();
        List<MyLocation> locations = new ArrayList<>();
        bundle.putSerializable(LOCATIONS, (Serializable) locations);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.locations = (List<MyLocation>) this.getArguments().get(LOCATIONS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_display, container, false);
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    public void setDisplayLocations(List<MyLocation> locations) {
        this.locations = locations;
        displayLocations();
    }

    public List<MyLocation> getLocations() {
        return locations;
    }

    /**
     * Display the locations currently held in this fragment
     */
    private void displayLocations() {
        // Only display with marker if map is not null and there are locations stored
        if (mMap != null && locations != null && locations.size() != 0) {
            mMap.clear();
            List<Marker> markers = new ArrayList<>();
            for (MyLocation myLocation: this.locations) {
                // TODO can build map (with icon) here
                markers.add(mMap.addMarker(new MarkerOptions()
                        .position(myLocation.getLatLng())
                        .title(myLocation.getName())
                        .snippet(myLocation.getAddress())));
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : markers) {
                builder.include(marker.getPosition());
            }
            int padding = 100; // offset from edges of the map in pixels
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.animateCamera(cu);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        displayLocations();
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this fragment to allow an
     * interaction in this fragment to be communicated to the activity and potentially other
     * fragments contained in that activity.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
