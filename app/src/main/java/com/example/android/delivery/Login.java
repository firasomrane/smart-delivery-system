package com.example.android.delivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by ASUS on 21/04/2018.
 */

public class Login extends AppCompatActivity {
    private static final String TAG = "Login";
    Button btnSignIn, btnSignUp;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText email;
    private EditText pswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        mAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.editText);
        pswd = (EditText) findViewById(R.id.editText2);
        btnSignIn = (Button) findViewById(R.id.button);
        btnSignUp = (Button) findViewById(R.id.button2);

        // Set OnClick Listener on SignUp button
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /// Create Intent for SignUpActivity abd Start The Activity
                Intent intentSignUP = new Intent(getApplicationContext(), Create_account.class);
                startActivity(intentSignUP);
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null)
                {
                    Log.d(TAG, "onAuthStateChanged: logged in");
                    startActivity(new Intent(Login.this, Dashboard.class));

                }

            }
        };
    }
    @Override
    protected void onStart()
    {

        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    private void start() {
        String e = email.getText().toString();
        String p = pswd.getText().toString();
        if((TextUtils.isEmpty(e))||(TextUtils.isEmpty(p)))
        {
            Toast.makeText(Login.this, "Veuillez saisir vos données", Toast.LENGTH_LONG).show();
        }
        else
        {
            mAuth.signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful())
                    {
                        Toast.makeText(Login.this, "Vérifiez les données entrées", Toast.LENGTH_LONG).show();
                    }
                }
            });    }}
}
