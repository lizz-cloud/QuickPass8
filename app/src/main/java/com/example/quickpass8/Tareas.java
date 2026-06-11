package com.example.quickpass8;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quickpass8.bdQuickPass.ConexionSQLiteHelper;
import com.example.quickpass8.utilidades.Utilidades;

public class Tareas extends AppCompatActivity {

    LinearLayout contenedorNotas;
    int contadorNotas = 0;
    String boletaActual;
    SQLiteDatabase db;

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
        boletaActual = getIntent().getStringExtra("boleta");

        // Abrir base de datos
        ConexionSQLiteHelper helper = new ConexionSQLiteHelper(this, "quickpass.db", null, 2);
        db = helper.getWritableDatabase();

        // Cargar tareas guardadas de este alumno
        cargarTareas();

        // Botón agregar,  crea nota vacía y la guarda en BD
        findViewById(R.id.btnAgregar).setOnClickListener(v -> {
            long id = insertarTareaEnBD("", contadorNotas % imagenesNotas.length);
            agregarNotaEnVista(id, "", contadorNotas % imagenesNotas.length);
        });

        // Botón inicio
        findViewById(R.id.btnInicio).setOnClickListener(v -> {
            Intent intent = new Intent(Tareas.this, paginicio.class);
            intent.putExtra("boleta", boletaActual);
            startActivity(intent);
        });
    }

    // Lee todas las tareas del alumno desde SQLite y las muestra w
    private void cargarTareas() {
        Cursor cursor = db.query(
                Utilidades.TABLA_TAREAS,
                null,
                Utilidades.CAMPO_TAREA_BOLETA + " = ?",
                new String[]{boletaActual},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(Utilidades.CAMPO_TAREA_ID));
                String texto = cursor.getString(cursor.getColumnIndexOrThrow(Utilidades.CAMPO_TAREA_TEXTO));
                int imagenIndex = cursor.getInt(cursor.getColumnIndexOrThrow(Utilidades.CAMPO_TAREA_IMAGEN));
                agregarNotaEnVista(id, texto, imagenIndex);
            } while (cursor.moveToNext());
        }

        // Si no había tareas guardadas, mostrar 3 vacías, es mi logica :(
        if (contadorNotas == 0) {
            for (int i = 0; i < 3; i++) {
                long id = insertarTareaEnBD("", i % imagenesNotas.length);
                agregarNotaEnVista(id, "", i % imagenesNotas.length);
            }
        }

        cursor.close();
    }

    // Inserta una tarea nueva en SQLite y devuelve su ID obvio
    private long insertarTareaEnBD(String texto, int imagenIndex) {
        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_TAREA_BOLETA, boletaActual);
        values.put(Utilidades.CAMPO_TAREA_TEXTO, texto);
        values.put(Utilidades.CAMPO_TAREA_IMAGEN, imagenIndex);
        return db.insert(Utilidades.TABLA_TAREAS, null, values);
    }

    // Crea la vista de una nota y la agrega al contenedor del act
    private void agregarNotaEnVista(long tareaId, String texto, int imagenIndex) {
        contadorNotas++;

        View nota = LayoutInflater.from(this)
                .inflate(R.layout.nota_item, contenedorNotas, false);

        // Imagen del post-it
        ImageView imgNota = nota.findViewById(R.id.imgNota);
        imgNota.setImageResource(imagenesNotas[imagenIndex]);

        // Texto guardado
        EditText editNota = nota.findViewById(R.id.editNota);
        editNota.setText(texto);

        // Guardar cambios en BD mientras el usuario escribe
        editNota.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                ContentValues values = new ContentValues();
                values.put(Utilidades.CAMPO_TAREA_TEXTO, s.toString());
                db.update(
                        Utilidades.TABLA_TAREAS,
                        values,
                        Utilidades.CAMPO_TAREA_ID + " = ?",
                        new String[]{String.valueOf(tareaId)}
                );
            }
        });

        // Sincronizar altura de imagen con el EditText
        editNota.addOnLayoutChangeListener((v, left, top, right, bottom,
                                            oldLeft, oldTop, oldRight, oldBottom) -> {
            int alturaEdit = bottom - top;
            if (alturaEdit > 0) {
                ViewGroup.LayoutParams params = imgNota.getLayoutParams();
                params.height = alturaEdit + 40; // +40 para que se vea bien, es mi magia :D
                imgNota.setLayoutParams(params);
            }
        });

        // Botón eliminar, borra de BD y de la vista ahora si jajaj
        nota.findViewById(R.id.btnEliminar).setOnClickListener(v -> {
            db.delete(
                    Utilidades.TABLA_TAREAS,
                    Utilidades.CAMPO_TAREA_ID + " = ?",
                    new String[]{String.valueOf(tareaId)}
            );
            contenedorNotas.removeView(nota);
        });

        contenedorNotas.addView(nota);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) db.close();
    }
}