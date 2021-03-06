package com.altechonduras.calet.dialogs;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.altechonduras.calet.R;
import com.altechonduras.calet.Utilities;
import com.altechonduras.calet.objects.MGP;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Nery Ortez on 27-Sep-17.
 */

public class DialogMGP extends AlertDialog {
    private final EditText rda;
    private final EditText idSitio;
    private final EditText nombreSitio;
    private final EditText numeroTicket;
    private final EditText combustible;
    private final Button horaInicio;
    private final Button horaFinal;
    private final EditText gastoAcarreo;
    private final EditText comentarios;
    private final EditText fecha;
    private String key = "";
    private final OnClickListener borrar = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            new AlertDialog.Builder(getContext())
                    .setMessage("¿Está seguro de eliminar este reporte?\nÉsta operación no se puede deshacer.")
                    .setPositiveButton("Borrar", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseDatabase.getInstance().getReference(Utilities.getMGPdir(getContext())).child(key).setValue(null);
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        }
    };
    private boolean editando = false;
    private MGP item = new MGP();
    private final OnClickListener clikc = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            item.setCombustible(combustible.getText().toString());
            item.setIdSitio(idSitio.getText().toString());
            item.setNombreSitio(nombreSitio.getText().toString());
            item.setId(numeroTicket.getText().toString());
            item.setRDA(rda.getText().toString());

            item.setHoraInicio(horaInicio.getText().toString());
            item.setHoraFinal(horaFinal.getText().toString());

//            SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
//            item.setFecha(f.format(Calendar.getInstance().getTime()));
            item.setFecha(fecha.getText().toString());
            item.setComentarios(comentarios.getText().toString());
            item.setGastoAcarreo(gastoAcarreo.getText().toString());

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Utilities.getMGPdir(getContext()));
            reference.child("sent").setValue(false);

            if (!editando) {
                reference.push().setValue(item);
            } else {
                reference.child(key).setValue(item);
            }
        }
    };

    public DialogMGP(@NonNull final Context context, final MGP item, final String key, boolean edit) {
        this(context);
        this.item = item;
        rda.setText(item.getRDA());
        idSitio.setText(item.getIdSitio());
        nombreSitio.setText(item.getNombreSitio());
        numeroTicket.setText(item.getId());
        combustible.setText(item.getCombustible());
        horaInicio.setText(item.getHoraInicio());
        horaFinal.setText(item.getHoraFinal());

        fecha.setText(item.getFecha());
        comentarios.setText(item.getComentarios());
        gastoAcarreo.setText(item.getGastoAcarreo());

        this.key = key;
        editando = true;

        setButton(BUTTON_NEGATIVE, "Borrar", borrar);

        if(!edit) {
            setButton(BUTTON_NEUTRAL, "editar", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    new DialogMGP(context, item, key, true).show();
                }
            });
            rda.setKeyListener(null);
            idSitio.setKeyListener(null);
            nombreSitio.setKeyListener(null);
            numeroTicket.setKeyListener(null);
            combustible.setKeyListener(null);
            horaInicio.setOnClickListener(null);
            horaFinal.setOnClickListener(null);

            fecha.setOnClickListener(null);
            gastoAcarreo.setOnClickListener(null);
            comentarios.setOnClickListener(null);

            setButton(BUTTON_POSITIVE, "Compartir", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    copiar(item, getContext());
                }
            });
        }
    }

    public DialogMGP(@NonNull final Context context){
        super(context);
        setTitle(R.string.NuevoMGP);
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_mgp, null);

        setView(v);

        fecha = v.findViewById(R.id.fecha);
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        fecha.setText(f.format(Calendar.getInstance().getTime()));

        gastoAcarreo = v.findViewById(R.id.gastoAcarreo);
        comentarios = v.findViewById(R.id.comentarios);
        rda = v.findViewById(R.id.rda);
        idSitio = v.findViewById(R.id.id_sitio);
        nombreSitio = v.findViewById(R.id.nombre_sitio);
        numeroTicket = v.findViewById(R.id.autorizado);
        combustible = v.findViewById(R.id.combustible);
        horaInicio = v.findViewById(R.id.horaInicio);
        horaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String text = selectedHour + ":" + (selectedMinute < 10 ? "0" : "") + selectedMinute;
                        horaInicio.setText(text);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        horaFinal = v.findViewById(R.id.horaFinal);
        horaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String text = selectedHour + ":" + (selectedMinute < 10 ? "0" : "") + selectedMinute;
                        horaFinal.setText( text );
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });



        setButton(BUTTON_POSITIVE, "guardar", clikc);

        setButton(BUTTON_NEUTRAL, "cancelar", ((OnClickListener) null));

    }

    private void copiar(MGP item, Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");

        String body =
                "RDA: " + item.getRDA() +
                        "\nID de Sitio: " + item.getIdSitio() +
                        "\nNombre de Sitio: " + item.getNombreSitio() +
                        "\nNúmero de ticket: " + item.getId() +
                        "\nCombustible: " + item.getCombustible() +
                        "\nGasto Acarreo: " + item.getGastoAcarreo() +
                        "\nHora Inicial: " + item.getHoraInicio() +
                        "\nHora Final: " + item.getHoraFinal() +
                        "\nComentarios: " + item.getComentarios();
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(emailIntent, "Enviar a..."));
    }
}
