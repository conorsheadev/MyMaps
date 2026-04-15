package com.csws.mymaps.premenu;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.csws.mymaps.R;
import com.csws.mymaps.map.MapViewActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout emailLayout, passwordLayout, confirmPasswordLayout;
    private TextInputEditText editTextEmail, editTextPassword, editTextConfirmPassword;
    private MaterialButton buttonSignUp, textGoToSignIn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.premenu_signup);

        mAuth = FirebaseAuth.getInstance();

        // Bind layouts
        emailLayout = (TextInputLayout) findViewById(R.id.editTextLayoutEmailSignUp);
        passwordLayout = (TextInputLayout) findViewById(R.id.editTextLayoutPasswordSignUp);
        confirmPasswordLayout = (TextInputLayout) findViewById(R.id.editTextLayoutConfirmPassword);

        // Bind fields
        editTextEmail = findViewById(R.id.editTextEmailSignUp);
        editTextPassword = findViewById(R.id.editTextPasswordSignUp);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        // Bind buttons
        buttonSignUp = findViewById(R.id.buttonSignUp);
        textGoToSignIn = findViewById(R.id.textGoToSignIn);

        buttonSignUp.setOnClickListener(v -> registerUser());

        textGoToSignIn.setOnClickListener(v -> {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        });
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        // Clear previous errors
        emailLayout.setError(null);
        passwordLayout.setError(null);
        confirmPasswordLayout.setError(null);

        if (TextUtils.isEmpty(email)) {
            emailLayout.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError("Password is required");
            return;
        }

        if (password.length() < 6) {
            passwordLayout.setError("Password must be at least 6 characters");
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordLayout.setError("Passwords do not match");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this,
                                "Account created successfully!",
                                Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(this, MapViewActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this,
                                "Error: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
