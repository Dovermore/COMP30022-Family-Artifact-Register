package com.example.family_artifact_register.UI.ArtifactManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.family_artifact_register.IFragment;
import com.example.family_artifact_register.R;
import com.example.family_artifact_register.UI.MapServiceFragment.MapSearchDisplayFragment;
import com.example.family_artifact_register.UI.MapServiceFragment.MyLocation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.dmoral.toasty.Toasty;

public class NewArtifactHappenedLocationFragment extends Fragment implements IFragment {
    /**
     * class tag
     */
    public static final String TAG = NewArtifactHappenedLocationFragment.class.getSimpleName();

    private MapSearchDisplayFragment mapDisplaySearchFragment;

    public NewArtifactHappenedLocationFragment() {
        // required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_artifact_happened_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.artifact_where_happened_title);

        // search location fragment
        this.mapDisplaySearchFragment = MapSearchDisplayFragment.newInstance();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_new_artifact_happened_location_main_view, mapDisplaySearchFragment)
                .commit();

        // to next fragment
        FloatingActionButton confirm = view.findViewById(R.id.fragment_new_artifact_happened_location_floating_button_confirm);
        confirm.setOnClickListener(view1 -> {
//            ((HappenedTimeListener)getActivity()).setHappenedTimeCalender(happenedTime);
            // TODO store location in NewArtifactActivity
            MyLocation selectedLocation = mapDisplaySearchFragment.getSelectedLocation();
            if (selectedLocation != null) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack("next");
                fragmentTransaction.replace(R.id.activity_new_artifact_main_view, NewArtifactStoredLocationFragment.newInstance());
                fragmentTransaction.commit();
            } else {
                Toasty.error(getContext(), R.string.not_enough_location_warning, Toasty.LENGTH_SHORT)
                        .show();
            }
        });
    }

    public static NewArtifactHappenedLocationFragment newInstance() { return new NewArtifactHappenedLocationFragment(); }
}
