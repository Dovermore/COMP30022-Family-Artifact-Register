package com.example.family_artifact_register.UI.Event;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.family_artifact_register.FoundationLayer.EventModel.EventListener;
import com.example.family_artifact_register.IFragment;
import com.example.family_artifact_register.PresentationLayer.EventPreseneter.EventViewModel;
import com.example.family_artifact_register.R;
import com.example.family_artifact_register.UI.Util.EventAdapter;

public class RecommendedEventFragment extends Fragment implements IFragment, EventListener {
    /**
     * class tag
     */
    public static final String TAG = RecommendedEventFragment.class.getSimpleName();

    private RecyclerView recyclerView;

    private EventAdapter eventAdapter;

    public RecommendedEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.v(TAG, "RecommendedEventFragment  created");
        return inflater.inflate(R.layout.fragment_recommended_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.fragment_recommended_events_recycler_view);
        eventAdapter = new EventAdapter(EventViewModel.getInstance().getRecommendedEvent(),
                                        true,
                                        this
        );
        EventViewModel.getInstance().addObserver(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(eventAdapter);

    }

    /**
     * @return created me fragment
     */
    public static RecommendedEventFragment newInstance() { return new RecommendedEventFragment(); }

    @Override
    public void notifyEventsChange() {
        eventAdapter.setEventList(EventViewModel.getInstance().getRecommendedEvent());
    }

    @Override
    public void attendEvent(String eventId) {
        EventViewModel.getInstance().addAttendEvent(eventId);
    }

    @Override
    public void cancelEvent(String eventId) {
        // empty
    }
}