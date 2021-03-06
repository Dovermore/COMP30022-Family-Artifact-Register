package com.unimelb.family_artifact_register;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.unimelb.family_artifact_register.UI.Artifact.ArtifactHub.HubActivity;
import com.unimelb.family_artifact_register.UI.Artifact.NewArtifact.ArtifactManageActivity;
import com.unimelb.family_artifact_register.UI.Social.Friend.FriendActivity;
import com.unimelb.family_artifact_register.UI.Util.BaseSignOutActionBarActivity;

import java.util.Arrays;
import java.util.List;

import static com.unimelb.family_artifact_register.Util.ActivityNavigator.navigateFromTo;

/**
 * @author XuLin Yang 904904,
 * @time 2019-8-10 17:01:49
 * @description main activity let user to choose to sign in or sign up
 * <p>
 * Deprecated because we decide to separate authenticate logic to a separate class Not Deleted by
 * open-close principle.
 */
@Deprecated
public class MainActivity extends BaseSignOutActionBarActivity {
    public static final int RC_SIGN_IN = 1;
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().build());
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set firebase sign in layout
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Toast.makeText(MainActivity.this, "User Signed In", Toast.LENGTH_SHORT).show();
            } else {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setAvailableProviders(providers)
                                .setLogo(R.drawable.icon_forget_me_not_1)
                                .build(),
                        RC_SIGN_IN
                );

            }
        };

        // set main activity layout
        // 1. centered title
        // 2. no navigation icon
        setCenterTitleText(R.string.app_name);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public void baseFinish() {
        mFirebaseAuth.signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);

    }

    /* ********************************** view controller *************************************** */

    public void manageArtifact(View view) {
        navigateFromTo(this, ArtifactManageActivity.class);
    }

    public void artifactHub(View view) {
        navigateFromTo(this, HubActivity.class);
    }

    public void social(View view) {
        navigateFromTo(this, FriendActivity.class);
    }
}
