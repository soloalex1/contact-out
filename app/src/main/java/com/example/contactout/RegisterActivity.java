package com.example.contactout;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.contactout.model.Conector;
import com.example.contactout.model.Contato;
import com.example.contactout.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputNewLogin;
    private EditText inputNewPassword;
    private EditText confirmNewPassword;
    private Button btnRegister;
    private FirebaseAuth auth;
    private DatabaseReference db;

    @Override
    protected void onStart(){
        super.onStart();
        auth = Conector.getFirebaseAuth();
        db = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputNewLogin = findViewById(R.id.inputLogin);
        inputNewPassword = findViewById(R.id.inputPassword);
        confirmNewPassword = findViewById(R.id.inputPassword);

        btnRegister = findViewById(R.id.btnCriarConta);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String email = inputNewLogin.getText().toString().trim();
        String password = inputNewPassword.getText().toString().trim();
        String confirmPassword = confirmNewPassword.getText().toString().trim();
        boolean isValidEmail, isValidPassword;

        if (email.contains("!") || email.contains("&") || email.contains("(") || email.contains("?") ||
                email.contains("%") || email.contains(")") || email.contains("[") || email.contains("]") ||
                email.contains("{") || email.contains("}")) {
            isValidEmail = false;
            inputNewLogin.setError("Email contém caracteres não permitidos.");
        } else {
            isValidEmail = true;
        }

        if (email == null || email.isEmpty()) {
            isValidEmail = false;
            inputNewLogin.setError("Campo obrigatório.");
        }

        if (!password.equals(confirmPassword)) {
            isValidPassword = false;
            inputNewPassword.setError("As senhas não coincidem.");
            confirmNewPassword.setError("As senhas não coincidem.");
        } else {
            if ((password == null || password.isEmpty()) || (confirmPassword == null || confirmPassword.isEmpty())) {
                isValidPassword = false;
                inputNewPassword.setError("Campo obrigatório.");
            } else isValidPassword = true;
        }

        if (isValidPassword && isValidEmail) createAccount(email, password);
    }

    private void createAccount(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = task.getResult().getUser().getUid();
                            String nome = inputNewLogin.getText().toString().trim();
                            fillUser(uid, nome);
                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(i);
                            Toast.makeText(RegisterActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                            auth.signOut();
                            finish();
                        }
                    }
                }
            ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fillUser(String uid, String login) {
        List<Contato> listaContatos = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            listaContatos.add(new Contato("", 0));
        }

        Usuario usr = new Usuario(uid, login);

        db.child("usuarios").child(uid).setValue(usr);
        db.child("usuarios").child(uid).child("listaContatos").setValue(listaContatos);
    }
}
