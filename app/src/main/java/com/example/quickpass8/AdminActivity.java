package com.example.quickpass8;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quickpass8.bdQuickPass.ConexionSQLiteHelper;
import com.example.quickpass8.entidades.Alumno;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    ListView listAlumnos;
    EditText etBuscar;
    ConexionSQLiteHelper conn;
    ArrayList<Alumno> listaCompleta = new ArrayList<>();
    AlumnoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        listAlumnos = findViewById(R.id.listAlumnos);
        etBuscar = findViewById(R.id.etBuscar);
        conn = new ConexionSQLiteHelper(this, "bdQuickPass", null, 2);

        cargarAlumnos();

        // Buscador en tiempo real
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrar(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        findViewById(R.id.btnSalir).setOnClickListener(v -> finish());
    }

    private void cargarAlumnos() {
        listaCompleta.clear();
        SQLiteDatabase bd = conn.getReadableDatabase();
        Cursor cursor = bd.rawQuery("SELECT * FROM Alumno", null);

        if (cursor.moveToFirst()) {
            do {
                Alumno a = new Alumno(
                        cursor.getInt(0),      // id_alumno
                        cursor.getString(1),   // tipoUsuario
                        cursor.getString(2),   // nombrecompleto
                        cursor.getString(3),   // boleta
                        cursor.getString(4),   // contrasena
                        cursor.getString(5),   // grupo
                        cursor.getString(6),   // turno
                        cursor.getString(7),   // semestre
                        cursor.getString(8)    // carrera
                );
                listaCompleta.add(a);
            } while (cursor.moveToNext());
        }
        cursor.close();
        bd.close();

        adapter = new AlumnoAdapter(this, new ArrayList<>(listaCompleta));
        listAlumnos.setAdapter(adapter);
    }

    private void filtrar(String texto) {
        ArrayList<Alumno> filtrada = new ArrayList<>();
        for (Alumno a : listaCompleta) {
            if (a.getNombre().toLowerCase().contains(texto.toLowerCase()) ||
                    a.getBoleta().toLowerCase().contains(texto.toLowerCase())) {
                filtrada.add(a);
            }
        }
        adapter.actualizar(filtrada);
    }

    private void mostrarDialogoEditar(Alumno alumno) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_editar_alumno, null);

        EditText etNombre   = view.findViewById(R.id.etEditNombre);
        EditText etBoleta   = view.findViewById(R.id.etEditBoleta);
        EditText etGrupo    = view.findViewById(R.id.etEditGrupo);
        EditText etTurno    = view.findViewById(R.id.etEditTurno);
        EditText etSemestre = view.findViewById(R.id.etEditSemestre);
        EditText etCarrera  = view.findViewById(R.id.etEditCarrera);

        etNombre.setText(alumno.getNombre());
        etBoleta.setText(alumno.getBoleta());
        etGrupo.setText(alumno.getGrupo());
        etTurno.setText(alumno.getTurno());
        etSemestre.setText(alumno.getSemestre());
        etCarrera.setText(alumno.getCarrera());

        new AlertDialog.Builder(this)
                .setTitle("Editar alumno")
                .setView(view)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    SQLiteDatabase bd = conn.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put("nombrecompleto", etNombre.getText().toString());
                    cv.put("boleta", etBoleta.getText().toString());
                    cv.put("grupo", etGrupo.getText().toString());
                    cv.put("turno", etTurno.getText().toString());
                    cv.put("semestre", etSemestre.getText().toString());
                    cv.put("carrera", etCarrera.getText().toString());

                    bd.update("Alumno", cv, "id_alumno=?",
                            new String[]{alumno.getId_alumno().toString()});
                    bd.close();

                    Toast.makeText(this, "Alumno actualizado ✓", Toast.LENGTH_SHORT).show();
                    cargarAlumnos();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void confirmarEliminar(Alumno alumno) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar alumno")
                .setMessage("¿Seguro que deseas eliminar a " + alumno.getNombre() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    SQLiteDatabase bd = conn.getWritableDatabase();
                    bd.delete("Alumno", "id_alumno=?",
                            new String[]{alumno.getId_alumno().toString()});
                    bd.close();
                    Toast.makeText(this, "Alumno eliminado", Toast.LENGTH_SHORT).show();
                    cargarAlumnos();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // ── Adapter ──
    class AlumnoAdapter extends ArrayAdapter<Alumno> {
        ArrayList<Alumno> lista;

        AlumnoAdapter(Context ctx, ArrayList<Alumno> lista) {
            super(ctx, 0, lista);
            this.lista = lista;
        }

        void actualizar(ArrayList<Alumno> nueva) {
            lista.clear();
            lista.addAll(nueva);
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.item_alumno, parent, false);

            Alumno a = lista.get(position);

            ((TextView) convertView.findViewById(R.id.tvNombre)).setText(a.getNombre());
            ((TextView) convertView.findViewById(R.id.tvTipo)).setText(a.getTipoUsuario());
            ((TextView) convertView.findViewById(R.id.tvBoleta)).setText("Boleta: " + a.getBoleta());
            ((TextView) convertView.findViewById(R.id.tvDetalle)).setText(
                    "Grupo: " + a.getGrupo() + " · " + a.getSemestre() + " sem · " + a.getCarrera()
            );

            convertView.findViewById(R.id.btnEditar).setOnClickListener(v ->
                    mostrarDialogoEditar(a));

            convertView.findViewById(R.id.btnEliminar).setOnClickListener(v ->
                    confirmarEliminar(a));

            return convertView;
        }
    }
}