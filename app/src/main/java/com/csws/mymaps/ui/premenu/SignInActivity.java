package com.csws.mymaps.ui.premenu;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.csws.mymaps.R;
import com.csws.mymaps.ui.map.MapViewActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private TextInputLayout emailLayout, passwordLayout;
    private TextInputEditText editTextEmail, editTextPassword;
    private MaterialButton buttonSignIn, textGoToSignUp;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.premenu_signin);

        mAuth = FirebaseAuth.getInstance();

        // Bind layouts
        emailLayout = findViewById(R.id.editTextLayoutEmail);

        passwordLayout = findViewById(R.id.editTextLayoutPassword);

        // Bind fields
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        // Bind buttons
        buttonSignIn = findViewById(R.id.buttonSignIn);
        textGoToSignUp = findViewById(R.id.textGoToSignUp);

        buttonSignIn.setOnClickListener(v -> signIn());

        textGoToSignUp.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
        });
    }

    private void signIn() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Clear previous errors
        emailLayout.setError(null);
        passwordLayout.setError(null);

        if (TextUtils.isEmpty(email)) {
            emailLayout.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError("Password is required");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MapViewActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this,
                                "Login failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
