package com.example.family_artifact_register.UI.ArtifactManager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.family_artifact_register.IFragment;
import com.example.family_artifact_register.R;
import com.example.family_artifact_register.UI.Util.NewTimelineListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.example.family_artifact_register.UI.Util.NewTimelineListener.NEW_ARTIFACT_TIMELINE;

/**
 * @author XuLin Yang 904904,
 * @time 2019-9-30 14:14:19
 * @description fragment to let user to choose the timeline for the new artifact
 */
public class NewArtifactChooseTimelineFragment extends Fragment implements IFragment {
    /**
     * class tag
     */
    private static final String TAG = NewArtifactChooseTimelineFragment.class.getSimpleName();

    private FloatingActionButton newTimelineConfirmButton;

    private FloatingActionButton existingTimelineConfirmButton;

    private RadioButton newTimelineButton;

    private RadioButton existingTimelineButton;

    private EditText newTimelineTitleEditText;

    private Spinner existingTimelineSpinner;

    private List<String> timelineTitles;

    private String slectedTimelineTitle = null;

    public NewArtifactChooseTimelineFragment() {
        // required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_artifact_choose_timeline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.artifact_choose_timeline);

        newTimelineButton = view.findViewById(R.id.new_artifact_new_timeline_radio_button);
        newTimelineButton.setOnClickListener(view1 -> {
            onRadioButtonClicked(view1);
        });
        existingTimelineButton = view.findViewById(R.id.new_artifact_existing_timeline_radio_button);
        existingTimelineButton.setOnClickListener(view1 -> {
            onRadioButtonClicked(view1);
        });

        newTimelineTitleEditText = view.findViewById(R.id.new_timeline_title_edit_text);

        existingTimelineSpinner = view.findViewById(R.id.existing_timeline_spinner);

        // TODO pull existing timeline data from server
        timelineTitles = new ArrayList<>();
        timelineTitles.add("timeline1");
        timelineTitles.add("timeline2");
        timelineTitles.add("timeline3");

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
            android.R.layout.simple_spinner_item, timelineTitles);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        // Apply the adapter to the spinner
        existingTimelineSpinner.setAdapter(adapter);
        existingTimelineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * @param parent parent view which is the spinner
             * @param view spinner item's view
             * @param pos position of the item in the adapter
             * @param id the line number of the item, normally same as pos
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                slectedTimelineTitle = timelineTitles.get(pos);
                // Toast.makeText(getContext(), timelineTitles.get(pos), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        newTimelineConfirmButton = view.findViewById(R.id.fragment_new_artifact_choose_timeline_floating_button_new_timeline_confirm);
        newTimelineConfirmButton.setOnClickListener(view1 -> {
            String newTimelineTitle = newTimelineTitleEditText.getText().toString();

            // new timeline's title has non-empty length
            if (newTimelineTitle.isEmpty()) {
                Toasty.error(getContext(), R.string.new_artifact_new_timeline_empty_title_warning, Toasty.LENGTH_LONG)
                        .show();
            } else {
                // pass data to activity
                ((NewTimelineListener)getActivity()).setTimeline(NEW_ARTIFACT_TIMELINE, newTimelineTitle);
                // TODO call NewArtifactActivity method to start upload

                // finish the activity
                getActivity().finish();
            }
        });
        existingTimelineConfirmButton = view.findViewById(R.id.fragment_new_artifact_choose_timeline_floating_button_existing_timeline_confirm);

        // initially all views except radio buttons are invisible, one of them is visible only when
        // one method (one radio button is selected)
        disableVisibility();
    }

    public static NewArtifactChooseTimelineFragment newInstance() { return new NewArtifactChooseTimelineFragment(); }

    public void disableVisibility() {
        newTimelineConfirmButton.setVisibility(View.GONE);
        newTimelineTitleEditText.setVisibility(View.GONE);
        existingTimelineConfirmButton.setVisibility(View.GONE);
        existingTimelineSpinner.setVisibility(View.GONE);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.new_artifact_new_timeline_radio_button:
                if (checked) {
                    newTimelineConfirmButton.setVisibility(View.VISIBLE);
                    newTimelineTitleEditText.setVisibility(View.VISIBLE);
                    existingTimelineConfirmButton.setVisibility(View.GONE);
                    existingTimelineSpinner.setVisibility(View.GONE);
                }
                break;
            case R.id.new_artifact_existing_timeline_radio_button:
                if (checked) {
                    newTimelineConfirmButton.setVisibility(View.GONE);
                    newTimelineTitleEditText.setVisibility(View.GONE);
                    existingTimelineConfirmButton.setVisibility(View.VISIBLE);
                    existingTimelineSpinner.setVisibility(View.VISIBLE);
                }
                break;
            default:
                Log.e(getFragmentTag(), "unknown radio button clicked !!!");
                break;
        }
    }
}
