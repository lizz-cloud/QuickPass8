package com.example.quickpass8;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//import android.R;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quickpass8.bdQuickPass.ConexionSQLiteHelper;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class CodigoQR extends AppCompatActivity {

    Button button2;
    ImageView imgQR;
    TextView tvNombre, tvBoleta, tvGrupo, tvCarrera;
    ConexionSQLiteHelper conn;
    String boleta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo_qr);

        button2 = findViewById(R.id.button2);
        imgQR = findViewById(R.id.imgQR);

        tvNombre = findViewById(R.id.tvNombre);
        tvBoleta = findViewById(R.id.tvBoleta);
        tvGrupo = findViewById(R.id.tvGrupo);
        tvCarrera = findViewById(R.id.tvCarrera);


        conn = new ConexionSQLiteHelper(this, "bdQuickPass", null, 2);

        boleta = getIntent().getStringExtra("boleta");

        GenerarQR();
    }

    public void GenerarQR() {
        SQLiteDatabase bd = conn.getReadableDatabase();

        Cursor cursor = bd.rawQuery(
                "SELECT nombrecompleto, boleta, grupo, turno, semestre, carrera " +
                        "FROM Alumno WHERE boleta=?",
                new String[]{boleta}
        );

        if (cursor.moveToFirst()) {

            String nombre = cursor.getString(0);
            String boletaBD = cursor.getString(1);
            String grupo = cursor.getString(2);
            String turno = cursor.getString(3);
            String semestre = cursor.getString(4);
            String carrera = cursor.getString(5);

            tvNombre.setText(nombre);
            tvBoleta.setText(boletaBD);
            tvGrupo.setText(grupo);
            tvCarrera.setText(carrera);

            String datosQR =
                    "QuickPass8" +
                            "\nAlumno: " + nombre +
                            "\nBoleta: " + boletaBD +
                            "\nGrupo: " + grupo +
                            "\nTurno: " + turno +
                            "\nSemestre: " + semestre +
                            "\nCarrera: " + carrera +
                            "\nAcceso válido";

            try {
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.encodeBitmap(datosQR, BarcodeFormat.QR_CODE, 500, 500);
                imgQR.setImageBitmap(bitmap);
            } catch (Exception e) {
                Toast.makeText(this, "Error al generar QR", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "No se encontraron datos", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        bd.close();
    }

    public void Regresar(View view) {
        Intent regreso = new Intent(this, paginicio.class);
        regreso.putExtra("boleta", boleta);
        startActivity(regreso);
        finish();
    }
}