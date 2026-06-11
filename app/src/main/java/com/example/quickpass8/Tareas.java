package com.example.quickpass8;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Tareas extends AppCompatActivity {

    LinearLayout contenedorNotas;
    int contadorNotas = 0;




    // Imágenes de post-its que ya tienes en drawable
    int[] imagenesNotas = {
            R.drawable.btninotuno,
            R.drawable.btninotdos,
            R.drawable.btninottres
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tareas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        contenedorNotas = findViewById(R.id.contenedorNotas);

        // 3 notas iniciales
        agregarNota();
        agregarNota();
        agregarNota();

        // Botón agregar
        findViewById(R.id.btnAgregar).setOnClickListener(v -> agregarNota());

        // Recibir boleta al entrar a Tareas
        String boleta = getIntent().getStringExtra("boleta");

        // Botón inicio — mandar boleta de regreso
        findViewById(R.id.btnInicio).setOnClickListener(v -> {
            Intent intent = new Intent(Tareas.this, paginicio.class);
            intent.putExtra("boleta", boleta); // ← agregar esto
            startActivity(intent);
        });
    }

    private void agregarNota() {
        contadorNotas++;

        View nota = LayoutInflater.from(this)
                .inflate(R.layout.nota_item, contenedorNotas, false);

        // Asignar imagen del post-it según el turno
        ImageView imgNota = nota.findViewById(R.id.imgNota);
        int imagenIndex = (contadorNotas - 1) % imagenesNotas.length;
        imgNota.setImageResource(imagenesNotas[imagenIndex]);

        // Botón eliminar
        nota.findViewById(R.id.btnEliminar).setOnClickListener(v -> {
            contenedorNotas.removeView(nota);
        });

        contenedorNotas.addView(nota);
    }
}