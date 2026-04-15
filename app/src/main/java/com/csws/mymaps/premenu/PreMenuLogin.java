package com.csws.mymaps.premenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.csws.mymaps.R;
import com.csws.mymaps.map.MapViewActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PreMenuLogin extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private TextView textQuickSignOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.premenu_loading);
        mAuth = FirebaseAuth.getInstance();

        textQuickSignOut = findViewById(R.id.textQuickSignOut);
        textQuickSignOut.setOnClickListener(v -> {
            mAuth.signOut();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            Log.d("PreMenuLogin", "Current user after signOut(): " + currentUser);
        });

        findViewById(android.R.id.content).postDelayed(this::checkLoginState, 1000);
    }

    public void checkLoginState()
    {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Case 1: Already signed in
            startActivity(new Intent(this, MapViewActivity.class));
        }
        else {
            SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
            String lastEmail = prefs.getString("lastEmail", null);

            if(lastEmail!=null)
            {
                // Case 2: Remembered User not signed in
                startActivity(new Intent(this, SignInActivity.class));
            }
            else
            {
                // Case 3: No Previous User
                startActivity(new Intent(this, SignUpActivity.class));
            }
        }

        finish();
    }


}
