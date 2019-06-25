package com.example.contactout;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactout.model.Contato;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnCall1, btnCall2, btnCall3, btnCall4, btnCall5;
    private TextView contactName1, contactName2, contactName3, contactName4, contactName5;
    private Button btnAddContact;

    static final int GET_CONTACT = 1;
    static final int ADD_CONTACT = 2;

    private Contato[] listaContatos;

    private FirebaseUser usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaContatos = new Contato[5];

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

        // Listeners
        btnCall1.setOnClickListener(this);
        btnCall2.setOnClickListener(this);
        btnCall3.setOnClickListener(this);
        btnCall4.setOnClickListener(this);
        btnCall5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnCall1:
                // TODO
                break;
            case R.id.btnCall2:
                // TODO
                break;
            case R.id.btnCall3:
                // TODO
                break;
            case R.id.btnCall4:
                // TODO
                break;
            case R.id.btnCall5:
                // TODO
                break;
            case R.id.btnAddContact:
                // TODO
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
                        if(listaContatos[j].getNome().equals("")){
                            listaContatos[j].setNome(contactName);
                            listaContatos[j].setNumero(Long.valueOf(contactNumber));
                            aux = j;
                            break;
                        }
                    }
                }
            }

//            saveData();
//            reload();
//            updateUI();

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
            listaContatos[x] = new Contato(sp.getString(nameKey, ""), sp.getLong(numberKey, 0));
        }
    }


    // método para abrir um contato
    private void getContact(){
        Intent i = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
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
        i.setData(Uri.parse("tel:" + listaContatos[index].getNumero()));

        // verificando permissões de acesso do aplicativo
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else startActivity(i);
    }
}