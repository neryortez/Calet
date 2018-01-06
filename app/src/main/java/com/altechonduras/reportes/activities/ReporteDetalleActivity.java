package com.altechonduras.reportes.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.altechonduras.reportes.R;
import com.altechonduras.reportes.Utilities;
import com.altechonduras.reportes.objects.Reporte;
import com.altechonduras.reportes.views.ListaAdapter;
import com.altechonduras.reportes.views.MultipleView;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nery Ortez on 27-Sep-17
 */

public class ReporteDetalleActivity extends AppCompatActivity {
    public static final String REFERENCE_REPORTE_FORMAT = "referencia";
    public static final String REFERENCE_REPORTE_DATA = "referencia_item";
    public static final String EDIT = "edit";
    public static final String ITEM_KEY = "key";

    private static final String BUTTON_NEGATIVE = "negative";
    private static final String BUTTON_NEUTRAL = "neutral";
    private static final String BUTTON_POSITIVE = "positive";

    private final Map<String, View> viewsMap = new HashMap<>();
    private String ref;
    private Reporte reporte;
    private String key = "";
    private final View.OnClickListener borrar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(ReporteDetalleActivity.this)
                    .setMessage("¿Está seguro de eliminar este reporte?\nÉsta operación no se puede deshacer.")
                    .setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseDatabase.getInstance().getReference(ref).child(key).setValue(null);
                                    finish();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        }
    };

    private boolean editando = false;
    private Map<String, Object> item = new HashMap<String, Object>();

    private final View.OnClickListener guardar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            item = new HashMap<>();
            for (String key : viewsMap.keySet()) {
                Object type = reporte.getFormat().get(key).get("type");
                if (type.equals("multiple")) {
                    item.put(key, ((MultipleView) viewsMap.get(key)).getMapValues());
                } else  if (type.equals("lista")) {
                    item.put(key, ((ListaAdapter) ((RecyclerView) viewsMap.get(key).findViewById(R.id.recyclerview)).getAdapter()).getLista());
                } else {
                    item.put(key, ((TextInputLayout) viewsMap.get(key).findViewById(R.id.key)).getEditText().getText().toString());
                }
            }

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(ref);
            reference.child("sent").setValue(false);

            Task<Void> saveTask;
            saveTask = !editando ? reference.push().setValue(item) : reference.child(key).setValue(item);
                    finish();
        }
    };
    private Button negativeButton;
    private Button positiveButton;
    private Button neutralButton;
    private View.OnClickListener cancel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ReporteDetalleActivity.this.finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String refFormat = getIntent().getStringExtra(REFERENCE_REPORTE_FORMAT);
        final String refData = getIntent().getStringExtra(REFERENCE_REPORTE_DATA);
        final String key = getIntent().getStringExtra(ITEM_KEY);
        FirebaseDatabase.getInstance().getReference(refFormat).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                ReporteDetalleActivity.this.reporte = dataSnapshot.getValue(Reporte.class);
                if (key != null) {
                    dataSnapshot.getRef().getRoot().child(refData).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override public void onDataChange(DataSnapshot dataSnapshot) {
                            ReporteDetalleActivity.this.init(ReporteDetalleActivity.this,
                                    (HashMap<String, Object>) dataSnapshot.getValue(),
                                    refData, dataSnapshot.getKey(), reporte, getIntent().getBooleanExtra(EDIT, false));
                        } @Override public void onCancelled(DatabaseError databaseError) {}
                    });
                } else {
                    init(ReporteDetalleActivity.this, refData, reporte);
                }
            } @Override public void onCancelled(DatabaseError databaseError) { }
        });
    }


    private void init(@NonNull final Context context, final Map<String, Object> item,
                 final String referencia, final String key, final Reporte reporte, boolean edit) {
        this.init(context, referencia, reporte);
        this.item = item;

        for (String llave : reporte.getFormat().keySet()) {
            Object type = reporte.getFormat().get(llave).get("type");
            if (type.equals("multiple")) {
                if (item.get(llave) instanceof HashMap) {
                    ((MultipleView) viewsMap.get(llave)).setValues(((HashMap) item.get(llave)));
                }
            } else if(type.equals("lista")) {
                final RecyclerView recycler = viewsMap.get(llave).findViewById(R.id.recyclerview);
                ((ListaAdapter) recycler.getAdapter()).setLista(((ArrayList) item.get(llave)));
            } else {
                ((TextInputEditText) viewsMap.get(llave).findViewById(R.id.value))
                        .setText(((String) item.get(llave)));
            }
        }

        this.key = key;
        editando = true;

        setButton(BUTTON_NEGATIVE, "Borrar", borrar);

        if(!edit) {
            setButton(BUTTON_NEUTRAL, "editar", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setEditar(true);
                }
            });

            for (View view : viewsMap.values()) {
                if (view instanceof ConstraintLayout) {
                    ((TextInputEditText) view.findViewById(R.id.value)).setKeyListener(null);
                } else if (view instanceof MultipleView){
                    view.setEnabled(false);
                } else if (view instanceof LinearLayout) {
                    view.findViewById(R.id.addOne).setClickable(false);
                    ((ListaAdapter) ((RecyclerView) view.findViewById(R.id.recyclerview)).getAdapter()).setEditable(false);
                }
            }

            setButton(BUTTON_POSITIVE, "Compartir", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    copiar(item, context);
                }
            });
        }
    }

    private void init(@NonNull final Context context, String referencia, Reporte reporte) {
        this.ref = referencia;
        this.reporte = reporte;

        LayoutInflater inflater = LayoutInflater.from(context);

        LinearLayout todoLayout = new LinearLayout(this);
        todoLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout contentLayout = new LinearLayout(context);
        contentLayout.setOrientation(LinearLayout.VERTICAL);

        View headerLayout = inflater.inflate(R.layout.header_reporte_detalles, null);
        neutralButton = headerLayout.findViewById(R.id.neutral);
        positiveButton = headerLayout.findViewById(R.id.positive);
        negativeButton = headerLayout.findViewById(R.id.negative);


        ScrollView scrollView = new ScrollView(context);
        scrollView.addView(contentLayout);

        todoLayout.addView(headerLayout);
        todoLayout.addView(scrollView);

        for (String key : reporte.getOrder()) {
            int id;
            HashMap<String, Object> campo = reporte.getFormat().get(key);
            switch (((String) campo.get("type"))) {
                case "string":
                    id = R.layout.type_string;
                    break;
                case "number":
                    id = R.layout.type_number;
                    break;
                case "long_string":
                    id = R.layout.type_long_string;
                    break;
                case "date":
                    id = R.layout.type_date;
                    break;
                case "time":
                    id = R.layout.type_time;
                    break;
                case "money":
                    id = R.layout.type_money;
                    break;
                case "multiple":
                    id = R.layout.type_multiple;
                    break;
                case "lista":
                    id = R.layout.type_lista;
                    break;
                default:
                    id = R.layout.type_string;
                    break;
            }
            View view = inflater.inflate(id, null);
            if (id == R.layout.type_date) {
                SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
                ((TextInputEditText) view.findViewById(R.id.value)).setText(f.format(Calendar.getInstance().getTime()));
            }
            if(id == R.layout.type_multiple){
                view = new MultipleView(context)
                        .setKeys(/*reporte.getMultiples().get(key)*/ ((ArrayList<String>) campo.get("campos")))
                        .setKey(key);
            } else if( id == R.layout.type_lista) {
                final RecyclerView recycler = view.findViewById(R.id.recyclerview);
                recycler.setLayoutManager(new LinearLayoutManager(context));
                recycler.setAdapter(
                        new ListaAdapter(
                                new ArrayList<HashMap<String, String>>(),
                                ((ArrayList<String>) campo.get("campos")),
                                context,
                                key, true));
                ((TextView) view.findViewById(R.id.title)).setText(key);
                view.findViewById(R.id.addOne).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((ListaAdapter) recycler.getAdapter()).addNew();
                    }
                });
            } else {
                ((TextInputLayout) view.findViewById(R.id.key)).setHint(key);
            }
            contentLayout.addView(view);
            viewsMap.put(key, view);
        }

        setContentView(todoLayout);

        setButton(BUTTON_POSITIVE, "guardar", guardar);

        setButton(BUTTON_NEUTRAL, "cancelar", cancel);

    }

    private void setEditar(boolean edit) {
        this.finish();
        Intent i = new Intent(this, ReporteDetalleActivity.class);
        i.putExtra(REFERENCE_REPORTE_FORMAT, Utilities.getFormatoReporte(reporte.getId(), this));
        i.putExtra(REFERENCE_REPORTE_DATA, ref);
        i.putExtra(ITEM_KEY, key);
        i.putExtra(EDIT, true);
        startActivity(i);
    }

    private void setButton(String button, String borrar, View.OnClickListener listener) {
        switch (button) {
            case BUTTON_POSITIVE:
                positiveButton.setVisibility(View.VISIBLE);
                positiveButton.setText(borrar);
                positiveButton.setOnClickListener(listener);
                break;
            case BUTTON_NEGATIVE:
                negativeButton.setVisibility(View.VISIBLE);
                negativeButton.setText(borrar);
                negativeButton.setOnClickListener(listener);
                break;
            case BUTTON_NEUTRAL:
                neutralButton.setVisibility(View.VISIBLE);
                neutralButton.setText(borrar);
                neutralButton.setOnClickListener(listener);
                break;
        }
    }

    private void copiar(Map<String, Object> item, Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");

        StringBuilder body = new StringBuilder();
        for (String s : reporte.getOrder()) {
            body.append("\n").append(s).append(": ").append(item.get(s));
        }
        emailIntent.putExtra(Intent.EXTRA_TEXT, body.toString());
        context.startActivity(Intent.createChooser(emailIntent, "Enviar a..."));
    }
}
