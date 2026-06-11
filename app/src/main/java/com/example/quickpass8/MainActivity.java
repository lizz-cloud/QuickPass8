package com.example.quickpass8;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quickpass8.bdQuickPass.ConexionSQLiteHelper;

public class MainActivity extends AppCompatActivity {

    EditText txtUsuario, txteContra;
    ConexionSQLiteHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtUsuario = findViewById(R.id.txtUsuario);
        txteContra = findViewById(R.id.txteContra);

        conn = new ConexionSQLiteHelper(this, "bdQuickPass", null, 2);

        SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);

        txtUsuario.setText(prefs.getString("boleta", ""));
        txteContra.setText(prefs.getString("contrasena", ""));
    }

    public void Iniciar(View view) {

        SQLiteDatabase db = conn.getReadableDatabase();

        String boleta = txtUsuario.getText().toString().trim();
        String contrasena = txteContra.getText().toString().trim();

        if(boleta.isEmpty() || contrasena.isEmpty()){

            Toast.makeText(this, "LLENA TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
            db.close();
            return;
        }

        String[] parametros = {boleta, contrasena};

        Cursor cursor = db.rawQuery(
                "SELECT * FROM Alumno WHERE boleta=? AND contrasena=?",
                parametros
        );

        if(cursor.moveToFirst()){

            String tipoUsuario = cursor.getString(cursor.getColumnIndexOrThrow("tipo_usuario"));

            SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putString("boleta", boleta);
            editor.putString("contrasena", contrasena);
            editor.apply();

            if(tipoUsuario.equals("Prefecto")){

                Intent i = new Intent(this, AdminActivity.class);
                i.putExtra("boleta", boleta);
                startActivity(i);

            }else{

                Intent i = new Intent(this, paginicio.class);
                i.putExtra("boleta", boleta);
                startActivity(i);
            }

        }else{

            Toast.makeText(this, "BOLETA/TRABAJADOR O CONTRASEÑA INCORRECTO", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }

    public void Registro(View view) {

        Intent i = new Intent(this, registro.class);
        startActivity(i);
    }
    public void OlvidoContraseña(View view){
        Toast.makeText(this, "Contacta al prefecto para recuperar contraseña", Toast.LENGTH_SHORT).show();
    }
}

