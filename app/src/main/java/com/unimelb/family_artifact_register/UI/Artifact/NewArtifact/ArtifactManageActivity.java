package com.unimelb.family_artifact_register.UI.Artifact.NewArtifact;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.unimelb.family_artifact_register.R;

/**
 * @author XuLin Yang 904904,
 * @time 2019-9-13 21:33:40
 * @description activity for the user to manage artifacts Deprecated because change to
 * activity-fragment design not deleted by the open-close principle
 */
@Deprecated
public class ArtifactManageActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artifact_manage);
    }

    /* ********************************** view controller *************************************** */
    public void newArtifact(View view) {
    }
}
