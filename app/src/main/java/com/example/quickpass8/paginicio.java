package com.example.quickpass8;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.ImageButton;


public class paginicio extends AppCompatActivity {

    ImageButton btniHorario, btniTarea, btniCodigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_paginicio);

        btniCodigo = findViewById(R.id.btniCodigo);
        btniHorario = findViewById(R.id.btniHorario);
        btniTarea = findViewById(R.id.btniTarea);

    }
    public void Codigo(View view){
        String boleta = getIntent().getStringExtra("boleta");
        Intent i =new Intent(this, CodigoQR.class);
        i.putExtra("boleta",boleta);
        startActivity(i);
    }

    public void Tarea(View view){
        String boleta = getIntent().getStringExtra("boleta");
        Intent i = new Intent(this, Tareas.class);
        i.putExtra("boleta", boleta); // ← agregar esto
        startActivity(i);
    }

    public void Horario(View view){
        String boleta = getIntent().getStringExtra("boleta");
        Intent i = new Intent(this, Horario.class);
        i.putExtra("boleta", boleta); // ← por si Horario también la necesita
        startActivity(i);
    }
    public void Salir(View view){
        startActivity(new Intent(this, MainActivity.class ));
        finish();
    }
}