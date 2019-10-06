package com.example.family_artifact_register;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.family_artifact_register.FoundationLayer.Util.FirebaseAuthHelper;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class FamilyArtifactRegisterActivity extends AppCompatActivity {
    /**
     * class tag
     */
    private final String TAG = getClass().getSimpleName();

    /**
     * firebase authentication
     */
    private FirebaseAuth mFirebaseAuth;

    /**
     * firebase request code
     */
    public static final int RC_SIGN_IN = 1;

    /**
     * control firebase state info
     */
    private FirebaseAuth.AuthStateListener mAuthStateListner;

    /**
     * Available sign-in providers
     */
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_artifact_register);
        checkAndSignIn();
    }

    private void checkAndSignIn() {
        // set firebase sign in layout
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListner = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user!=null) {
                // Check user even if signed in to register him to database (if haven't)
                FirebaseAuthHelper.getInstance().checkRegisterUser(user);
                Toast.makeText(this, "User Signed In", Toast.LENGTH_SHORT).show();
                startHomeActivity();
            }
            else {
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
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Log.i(TAG, String.format("User signin - resultCode: %d", resultCode));
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser == null) {
                    Log.e(TAG, "Auth with FireBase failed!", new Throwable());
                    return;
                }
                // Successfully signed in, get user and start home activity
                FirebaseAuthHelper.getInstance().checkRegisterUser(firebaseUser);
                startHomeActivity();
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Log.e(TAG, "Auth failed, Error code: " + response.getError().getErrorCode());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListner);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListner);
    }
}