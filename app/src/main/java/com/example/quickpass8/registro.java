package com.example.quickpass8;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quickpass8.bdQuickPass.ConexionSQLiteHelper;

public class registro extends AppCompatActivity {

    EditText etNombre, etBoleta, etContrasena, etConfirmarContrasena;
    Spinner spinnerTipo, spinnerGrupo, spinnerSemestre, spinnerTurno, spinnerCarrera;
    ConexionSQLiteHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        spinnerTipo = findViewById(R.id.spinnerTipo);
        spinnerGrupo = findViewById(R.id.spinnerGrupo);
        spinnerSemestre = findViewById(R.id.spinnerSemestre);
        spinnerTurno = findViewById(R.id.spinnerTurno);
        spinnerCarrera = findViewById(R.id.spinnerCarrera);

        etNombre = findViewById(R.id.etNombre);
        etBoleta = findViewById(R.id.etBoleta);
        etContrasena = findViewById(R.id.etContrasena);
        etConfirmarContrasena = findViewById(R.id.etConfirmarContrasena);

        conn = new ConexionSQLiteHelper(this, "bdQuickPass", null, 2);

        cargarSpinners();
    }

    private void cargarSpinners() {

        String[] tipos = {"Alumno", "Prefecto"};
        String[] grupos = {"1", "2", "3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18"};
        String[] semestres = {"Primero", "Segundo", "Tercero", "Cuarto", "Quinto", "Sexto"};
        String[] turnos = {"Matutino", "Vespertino"};
        String[] carreras = {"Computación", "Mantenimiento Industrial", "Plásticos","Automotriz","Tronco Común"};

        colocarOpciones(spinnerTipo, tipos);
        colocarOpciones(spinnerGrupo, grupos);
        colocarOpciones(spinnerSemestre, semestres);
        colocarOpciones(spinnerTurno, turnos);
        colocarOpciones(spinnerCarrera, carreras);
    }

    private void colocarOpciones(Spinner spinner, String[] opciones) {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                opciones
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void Registrar(View view) {

        SQLiteDatabase bd = conn.getWritableDatabase();

        String tipoUsuario = spinnerTipo.getSelectedItem().toString();
        String nombre = etNombre.getText().toString().trim();
        String boleta = etBoleta.getText().toString().trim();
        String grupo = spinnerGrupo.getSelectedItem().toString();
        String semestre = spinnerSemestre.getSelectedItem().toString();
        String turno = spinnerTurno.getSelectedItem().toString();
        String carrera = spinnerCarrera.getSelectedItem().toString();
        String contrasena = etContrasena.getText().toString().trim();
        String confirmar = etConfirmarContrasena.getText().toString().trim();

        if(nombre.isEmpty() || boleta.isEmpty() || contrasena.isEmpty() || confirmar.isEmpty()){

            Toast.makeText(this, "LLENA TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
            bd.close();
            return;
        }

        if(!contrasena.equals(confirmar)){

            Toast.makeText(this, "LAS CONTRASEÑAS NO COINCIDEN", Toast.LENGTH_SHORT).show();
            bd.close();
            return;
        }

        Cursor cursor = bd.rawQuery(
                "SELECT * FROM Alumno WHERE boleta=?",
                new String[]{boleta}
        );

        if(cursor.moveToFirst()){

            Toast.makeText(this, "ESA BOLETA O NO. DE TRABAJADOR YA EXISTE", Toast.LENGTH_SHORT).show();
            cursor.close();
            bd.close();
            return;
        }

        cursor.close();

        ContentValues values = new ContentValues();

        values.put("tipo_usuario", tipoUsuario);
        values.put("nombrecompleto", nombre);
        values.put("boleta", boleta);
        values.put("contrasena", contrasena);
        values.put("grupo", grupo);
        values.put("turno", turno);
        values.put("semestre", semestre);
        values.put("carrera", carrera);

        long resultado = bd.insert("Alumno", null, values);

        if(resultado == -1){

            Toast.makeText(this, "ERROR AL REGISTRAR", Toast.LENGTH_SHORT).show();

        }else{

            Toast.makeText(this, "REGISTRO EXITOSO", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }

        bd.close();
    }

    public void Inicio(View view) {

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}

