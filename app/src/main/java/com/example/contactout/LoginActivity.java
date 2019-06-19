package com.example.contactout;

import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener{


    private TextView mStatusTextView;
    private EditText mEmailField;
    private EditText mPasswordField;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    TextToSpeech ttobj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views
        mStatusTextView = findViewById(R.id.status);
        mEmailField = findViewById(R.id.emailField);
        mPasswordField = findViewById(R.id.password);

        // Buttons
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_create_account_button).setOnClickListener(this);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        ttobj = new TextToSpeech(this, this);
    }
    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        // FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
    // [END on_start_check_user]

    public void updateUI(FirebaseUser fUser){
        if (fUser !=null) {
            mStatusTextView.setText("Sucesso no Cadastro! Uid:" + fUser.getUid());
            fUser.sendEmailVerification();
        } else   mStatusTextView.setText("Erro no Cadastro");

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.email_create_account_button) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.email_sign_in_button) {
            //signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());

            //int i= 1;
            ttobj.speak("Função não implementada", TextToSpeech.QUEUE_FLUSH,null, "1");
            mStatusTextView.setText("Função não implementada");
        }
    }

    private void createAccount(String email, String password) {
        Log.d("appLS", "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        //       showProgressDialog();
        // [START create_user_with_email]
        Task t=mAuth.createUserWithEmailAndPassword(email, password);
        t.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("appLS", "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("appLS", "createUserWithEmail:failure", task.getException());

                    updateUI(null);
                }

            }
        });
        // [END create_user_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Requirido.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Requirido.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    @Override
    public void onInit(int status) {
        mStatusTextView.setText("Startando TTS");
        //ttobj.setLanguage(Locale.)
    }
}
