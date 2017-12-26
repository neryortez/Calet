package com.altechonduras.reportes.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.altechonduras.reportes.R;
import com.altechonduras.reportes.objects.Reporte;
import com.altechonduras.reportes.views.MultipleView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nery Ortez on 27-Sep-17.
 */

public class DialogReportes extends AlertDialog {
    private final Map<String, View> viewsMap = new HashMap<>();
    private final String ref;
    private final Reporte reporte;
    private String key = "";
    private final OnClickListener borrar = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            new AlertDialog.Builder(getContext())
                    .setMessage("¿Está seguro de eliminar este reporte?\nÉsta operación no se puede deshacer.")
                    .setPositiveButton("Borrar", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseDatabase.getInstance().getReference(ref).child(key).setValue(null);
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        }
    };
    private boolean editando = false;
    private Map<String, Object> item = new HashMap<String, Object>();
    private final OnClickListener clikc = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            item = new HashMap<>();
            for (String s : viewsMap.keySet()) {
                if (reporte.getFormat().get(s).equals("multiple")) {
                    item.put(s, ((MultipleView) viewsMap.get(s)).getMapValues());
                } else {
                    item.put(s, ((TextInputLayout) viewsMap.get(s).findViewById(R.id.key)).getEditText().getText().toString());
                }
            }

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(ref);
            reference.child("sent").setValue(false);

            if (!editando) {
                reference.push().setValue(item);
            } else {
                reference.child(key).setValue(item);
            }
        }
    };

    public DialogReportes(@NonNull final Context context, final Map<String, Object> item,
                          final String referencia, final String key, final Reporte reporte, boolean edit) {

        this(context, referencia, reporte);
        this.item = item;

        for (String llave : reporte.getFormat().keySet()) {
            if (reporte.getFormat().get(llave).equals("multiple")) {
                if (item.get(llave) instanceof HashMap) {
                    ((MultipleView) viewsMap.get(llave)).setValues(((HashMap) item.get(llave)));
                }
            } else {
                ((TextInputEditText) viewsMap.get(llave).findViewById(R.id.value)).setText(((String) item.get(llave)));
            }
        }

        this.key = key;
        editando = true;

        setButton(BUTTON_NEGATIVE, "Borrar", borrar);

        if(!edit) {
            setButton(BUTTON_NEUTRAL, "editar", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    new DialogReportes(context, item, ref, key, reporte, true).show();
                }
            });
            for (View view : viewsMap.values()) {
                if (view instanceof ConstraintLayout) {
                    ((TextInputEditText) view.findViewById(R.id.value)).setKeyListener(null);
                } else if (view instanceof MultipleView){
                    ((MultipleView) view).setEnabled(false);
                }
            }

            setButton(BUTTON_POSITIVE, "Compartir", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    copiar(item, getContext());
                }
            });
        }
    }

    public DialogReportes(@NonNull final Context context, String referencia, Reporte reporte) {
        super(context);
        setTitle(R.string.NuevoMGP);
        this.ref = referencia;
        this.reporte = reporte;
        setTitle("Nuevo");
//        keys.addAll(mformat.keySet());


        LayoutInflater inflater = LayoutInflater.from(context);
        ScrollView scrollView = new ScrollView(context);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(layout);
        for (String key : reporte.getOrder()) {
            int id = 0;
            switch (reporte.getFormat().get(key)) {
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
                default:
                    id = R.layout.type_string;
            }
            View view = inflater.inflate(id, null);
            if (id == R.layout.type_date) {
                SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
                ((TextInputEditText) view.findViewById(R.id.value)).setText(f.format(Calendar.getInstance().getTime()));
            }
            if(id == R.layout.type_multiple){
                view = new MultipleView(context)
                        .setKeys(reporte.getMultiples().get(key))
                        .setKey(key);
            } else {
                ((TextInputLayout) view.findViewById(R.id.key)).setHint(key);
            }
            layout.addView(view);
            viewsMap.put(key, view);
        }
//        View v = inflater.inflate(R.layout.dialog_mgp, null);

        setView(scrollView);

//        fecha = v.findViewById(R.id.fecha);
//        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
//        fecha.setText(f.viewsMap(Calendar.getInstance().getTime()));
//
//        gastoAcarreo = v.findViewById(R.id.gastoAcarreo);
//        comentarios = v.findViewById(R.id.comentarios);
//        rda = v.findViewById(R.id.rda);
//        idSitio = v.findViewById(R.id.id_sitio);
//        nombreSitio = v.findViewById(R.id.nombre_sitio);
//        numeroTicket = v.findViewById(R.id.autorizado);
//        combustible = v.findViewById(R.id.combustible);
//        horaInicio = v.findViewById(R.id.horaInicio);
//        horaInicio.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        String text = selectedHour + ":" + (selectedMinute < 10 ? "0" : "") + selectedMinute;
//                        horaInicio.setText(text);
//                    }
//                }, hour, minute, true);//Yes 24 hour time
//                mTimePicker.setTitle("Select Time");
//                mTimePicker.show();
//            }
//        });
//
//        horaFinal = v.findViewById(R.id.horaFinal);
//        horaFinal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        String text = selectedHour + ":" + (selectedMinute < 10 ? "0" : "") + selectedMinute;
//                        horaFinal.setText( text );
//                    }
//                }, hour, minute, true);//Yes 24 hour time
//                mTimePicker.setTitle("Select Time");
//                mTimePicker.show();
//            }
//        });



        setButton(BUTTON_POSITIVE, "guardar", clikc);

        setButton(BUTTON_NEUTRAL, "cancelar", ((OnClickListener) null));

    }

    private void copiar(Map<String, Object> item, Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");

        StringBuilder body = new StringBuilder();
        for (String s : reporte.getOrder()) {
            body.append("\n").append(s).append(": ").append(item.get(s));
        }
//                "RDA: " + item.getRDA() +
//                        "\nID de Sitio: " + item.getIdSitio() +
//                        "\nNombre de Sitio: " + item.getNombreSitio() +
//                        "\nNúmero de ticket: " + item.getId() +
//                        "\nCombustible: " + item.getCombustible() +
//                        "\nGasto Acarreo: " + item.getGastoAcarreo() +
//                        "\nHora Inicial: " + item.getHoraInicio() +
//                        "\nHora Final: " + item.getHoraFinal() +
//                        "\nComentarios: " + item.getComentarios();
        emailIntent.putExtra(Intent.EXTRA_TEXT, body.toString());
        context.startActivity(Intent.createChooser(emailIntent, "Enviar a..."));
    }
}
