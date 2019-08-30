package com.example.family_artifact_register;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.text.Editable;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import static com.example.family_artifact_register.util.ActivityNavigator.navigateFromTo;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // define constants required
        String[] genders = new String[] {"Male", "Female"};

        // define adapters required
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, genders);

        // define variables required
        final TextInputLayout usernameTextInput = findViewById(R.id.username_text_input);
        final TextInputEditText usernameEditText = findViewById(R.id.username_edit_text);

        final TextInputLayout emailTextInput = findViewById(R.id.email_text_input);
        final TextInputEditText emailEditText = findViewById(R.id.email_edit_text);

        final TextInputLayout passwordTextInput = findViewById(R.id.password_text_input);
        final TextInputEditText passwordEditText = findViewById(R.id.password_edit_text);

        final TextInputLayout passwordConfirmTextInput = findViewById(R.id.password_confirm_text_input);
        final TextInputEditText passwordConfirmEditText = findViewById(R.id.password_confirm_edit_text);

        final TextInputLayout genderTextInput = findViewById(R.id.gender_text_input);
        final AutoCompleteTextView genderTextView = findViewById(R.id.gender_dropdown_menu);

        final TextInputLayout numberTextInput = findViewById(R.id.number_text_input);
        final TextInputEditText numberEditText = findViewById(R.id.number_edit_text);

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // setup the content for drop down menu
        genderTextView.setAdapter(genderAdapter);

        // user click "Confirm" to be directed to sign in activity
        final MaterialButton confirmButton = findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean allInputValid = true;

                if(isEmpty(usernameEditText.getText())) {
                    usernameTextInput.setError(getString(R.string.error_sign_up_username));
                    allInputValid = false;
                }
                else { usernameTextInput.setError(null); }

                if(isEmpty(emailEditText.getText())) {
                    emailTextInput.setError(getString(R.string.error_sign_up_email));
                    allInputValid = false;
                }
                else { emailTextInput.setError(null); }

                if(isEmpty(passwordEditText.getText())) {
                    passwordTextInput.setError(getString(R.string.error_sign_up_password));
                    allInputValid = false;
                }
                else { passwordTextInput.setError(null); }

                if(isEmpty(passwordConfirmEditText.getText())) {
                    passwordConfirmTextInput.setError(getString(R.string.error_sign_up_password_confirm));
                    allInputValid = false;
                }
                else { passwordConfirmTextInput.setError(null); }

                if(isEmpty(genderTextView.getText())) {
                    genderTextInput.setError(getString(R.string.error_sign_up_gender));
                    allInputValid = false;
                }
                else { genderTextInput.setError(null); }

                if(isEmpty(numberEditText.getText())) {
                    numberTextInput.setError(getString(R.string.error_sign_up_number));
                    allInputValid = false;
                }
                else { numberTextInput.setError(null); }

                if(allInputValid) {
                    firebaseAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                            .addOnCompleteListener(
                                    new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                Log.d(TAG, "createUserWithEmail:success");
                                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                                navigateFromTo(SignUpActivity.this, SignInActivity.class);
                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                                Toast.makeText(SignUpActivity.this, "Registration failed.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                            );
                }
            }
        });

        // user click "Cancel" to be directed to main activity
        final MaterialButton cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                navigateFromTo(SignUpActivity.this, MainActivity.class);
            }
        });

        // clear the error message when username entered is valid
        usernameEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (!isEmpty(usernameEditText.getText()))
                    usernameTextInput.setError(null);
                return false;
            }
        });

        // clear the error message when email entered is valid
        emailEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (!isEmpty(emailEditText.getText()))
                    emailTextInput.setError(null);
                return false;
            }
        });

        // clear the error message when password entered is valid
        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (!isEmpty(passwordEditText.getText()))
                    passwordTextInput.setError(null);
                return false;
            }
        });

        // clear the error message when confirm password entered is valid
        passwordConfirmEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (!isEmpty(passwordConfirmEditText.getText()))
                    passwordConfirmTextInput.setError(null);
                return false;
            }
        });

        // clear the error message when gender entered is valid
        genderTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (!isEmpty(genderTextView.getText()))
                    genderTextInput.setError(null);
                return false;
            }
        });

        // clear the error message when phone entered is valid
        numberEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (!isEmpty(numberEditText.getText()))
                    numberTextInput.setError(null);
                return false;
            }
        });
    }

    // check if the input is empty
    private boolean isEmpty(@Nullable Editable text) {
        return text == null || text.length() == 0;
    }

}