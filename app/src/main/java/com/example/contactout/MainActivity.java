package com.example.contactout;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactout.model.Conector;
import com.example.contactout.model.Contato;
import com.example.contactout.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnCall1, btnCall2, btnCall3, btnCall4, btnCall5;
    private TextView contactName1, contactName2, contactName3, contactName4, contactName5;
    private TableRow row1, row2, row3, row4, row5;
    private Button btnAddContact;

    static final int GET_CONTACT = 1;
    static final int ADD_CONTACT = 2;

    private List<Contato> listaContatos;
    private String currentUser;

    private FirebaseUser usr;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    @Override
    protected void onStart(){
        super.onStart();
        auth = Conector.getFirebaseAuth();
        usr = Conector.getFirebaseUser();
        if(usr == null) {
            Toast.makeText(this, "Usuário nulo", Toast.LENGTH_SHORT).show();
            finish();
        }

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();

        getUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaContatos = new ArrayList<>(5);
        usr = FirebaseAuth.getInstance().getCurrentUser();

        // Botões
        btnCall1 = findViewById(R.id.btnCall1);
        btnCall2 = findViewById(R.id.btnCall2);
        btnCall3 = findViewById(R.id.btnCall3);
        btnCall4 = findViewById(R.id.btnCall4);
        btnCall5 = findViewById(R.id.btnCall5);
        btnAddContact = findViewById(R.id.btnAddContact);

        // TextViews
        contactName1 = findViewById(R.id.contactName1);
        contactName2 = findViewById(R.id.contactName2);
        contactName3 = findViewById(R.id.contactName3);
        contactName4 = findViewById(R.id.contactName4);
        contactName5 = findViewById(R.id.contactName5);

        // TableRows
        row1 = findViewById(R.id.contactRow1);
        row2 = findViewById(R.id.contactRow2);
        row3 = findViewById(R.id.contactRow3);
        row4 = findViewById(R.id.contactRow4);
        row5 = findViewById(R.id.contactRow5);

        // Listeners
        btnCall1.setOnClickListener(this);
        btnCall2.setOnClickListener(this);
        btnCall3.setOnClickListener(this);
        btnCall4.setOnClickListener(this);
        btnCall5.setOnClickListener(this);
    }


    public void getUser() {
        dbRef.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Usuario> userList = new ArrayList<Usuario>();
                userList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Usuario user = ds.getValue(Usuario.class);
                    userList.add(user);
                }

//                for (Usuario u : userList) {
////                    if (u.getId().equals(usr.getUid())) {
////                        contactName1.setText(u.getListaContatos().get(0).getNome());
////                        contactName2.setText(u.getListaContatos().get(1).getNome());
////                        contactName3.setText(u.getListaContatos().get(2).getNome());
////                        contactName4.setText(u.getListaContatos().get(3).getNome());
////                        contactName5.setText(u.getListaContatos().get(4).getNome());
////                    }
////                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnCall1:
                callContact(0);
                break;
            case R.id.btnCall2:
                callContact(1);
                break;
            case R.id.btnCall3:
                callContact(2);
                break;
            case R.id.btnCall4:
                callContact(3);
                break;
            case R.id.btnCall5:
                callContact(4);
                break;
            case R.id.btnAddContact:
                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(i);
                break;
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent i){
        if(reqCode == GET_CONTACT){
            if(resCode == RESULT_OK){
                Uri contactURI = i.getData();

                // projeções de nome e número
                String[] displayNameProjection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
                String[] numberProjection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

                // cursor pra percorrer os resultados de nome
                Cursor c = getContentResolver().query(contactURI, displayNameProjection, null, null, null);
                c.moveToFirst();

                // salvando nome
                String contactName = c.getString(0);

                // cursor pra percorrer os resultados de número
                c = getContentResolver().query(contactURI, numberProjection, null, null, null);
                c.moveToFirst();

                // salvando número (regex shit)
                int numCol = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String contactNumber = c.getString(numCol);

                int aux = 0;
                boolean exists = false;

                for(Contato contact : listaContatos){
                    if(contact.getNome().equals(contactName)){
                        exists = true;
                        return;
                    }
                }

                if(exists == false){
                    for(int j = 0; j < 5; j++){
                        if(listaContatos.get(j).getNome().equals("")){
                            listaContatos.get(j).setNome(contactName);
                            listaContatos.get(j).setNumero(Long.valueOf(contactNumber));
                            aux = j;
                            break;
                        }
                    }
                }
            }
            reload();
            updateUI(usr);
            if(resCode == RESULT_CANCELED) Toast.makeText(this, "Falha ao obter contatos.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveData(int i, String name, long number){
        String nameKey = "nome_" + i;
        String numberKey = "number_" + i;

        SharedPreferences sp = getApplicationContext().getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString(nameKey, name);
        e.putLong(numberKey, number);
        e.apply();
    }

    // recarrega dados dos contatos
    private void reload() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("prefs", MODE_PRIVATE);
        for (int x = 0; x < 5; x++){
            String nameKey = "nome_" + x;
            String numberKey = "number_" + x;
            listaContatos.add(new Contato(sp.getString(nameKey, ""), sp.getLong(numberKey, 0)));
        }
    }

    private void deleteContact(int id){
        SharedPreferences sp = getApplicationContext().getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();

        edit.remove("nome_" + id);
        edit.remove("number_" + id);
        edit.apply();

        listaContatos.get(id).setNome("");
        listaContatos.get(id).setNumero(0);

        reload();
    }

    private void updateUI(FirebaseUser usr){
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(usr.getUid())){

            // linha 1
            if(!listaContatos.get(0).getNome().isEmpty()){
                contactName1.setText(listaContatos.get(0).getNome());
            } else row1.setVisibility(View.GONE);


            // linha 2
            if(!listaContatos.get(1).getNome().isEmpty()){
                contactName2.setText(listaContatos.get(1).getNome());
            } else row2.setVisibility(View.GONE);

            // linha 3
            if(!listaContatos.get(2).getNome().isEmpty()){
                contactName3.setText(listaContatos.get(2).getNome());
            } else row3.setVisibility(View.GONE);

            // linha4
            if(!listaContatos.get(3).getNome().isEmpty()){
                contactName4.setText(listaContatos.get(3).getNome());
            } else row4.setVisibility(View.GONE);

            // linha5
            if(!listaContatos.get(4).getNome().isEmpty()){
                contactName5.setText(listaContatos.get(4).getNome());
            } else row5.setVisibility(View.GONE);
        }
    }

    // método para abrir um contato
    private void getContact(){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setDataAndType(Uri.parse("content://contacts"), ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(i, ADD_CONTACT);
    }

    // método para abrir o telefone
    private void openPhone(){
        Intent i = new Intent(Intent.ACTION_DIAL);
        startActivity(i);
    }

    // chama o contato a partir do índice passado
    private void callContact(int index){
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:" + listaContatos.get(index).getNumero()));

        // verificando permissões de acesso do aplicativo
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else startActivity(i);
    }
}