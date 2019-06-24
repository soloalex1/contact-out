package com.example.contactout;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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

                // TODO finalizar implementação do processamento de resultados
            }
        }
    }
}