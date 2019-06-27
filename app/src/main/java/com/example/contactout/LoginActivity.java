package com.example.contactout;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputLogin;
    private EditText inputPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Views
        inputLogin = findViewById(R.id.inputLogin);
        inputPassword = findViewById(R.id.inputPassword);

        // Botões
        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.btnCriarConta).setOnClickListener(this);

        // Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    // método que verifica se o usuário está logado e atualiza a UI de acordo
    @Override
    public void onStart() {
        super.onStart();
        updateUI(mAuth.getCurrentUser());
    }

    public void updateUI(FirebaseUser user){
        if (user != null && mAuth.getCurrentUser() == user) {
            // TODO
        } else Toast.makeText(LoginActivity.this, "Falha ao obter dados do usuário.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btnCriarConta) {
            createAccount();
        } else if (i == R.id.btnLogin) {
            signIn(inputLogin.getText().toString(), inputPassword.getText().toString());
        }
    }

    private void createAccount() {
        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(i);
    }

    private void signIn(String login, String password){
        // se o form for inválido, nada acontece feijoada
        if(!validateForm()) return;

        mAuth.signInWithEmailAndPassword(login, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){ // se efetuar o login, atualiza a UI com os contatos do usuário
//                    FirebaseUser user = mAuth.getCurrentUser();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
//                    updateUI(user);

                } else {  // se não, exibe uma mensagem de erro
                    Toast.makeText(LoginActivity.this, "Falha na autenticação.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // método de validação do formulário de login
    private boolean validateForm() {
        boolean valid = false;

        // login
        if (TextUtils.isEmpty(inputLogin.getText().toString())) {
            inputLogin.setError("Campo obrigatório.");
            valid = false;
        } else inputLogin.setError(null);

        // senha
        if (TextUtils.isEmpty(inputPassword.getText().toString())) {
            inputPassword.setError("Campo obrigatório.");
            valid = false;
        } else inputPassword.setError(null);
        return valid;
    }
}