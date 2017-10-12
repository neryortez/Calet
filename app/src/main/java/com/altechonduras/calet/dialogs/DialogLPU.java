package com.altechonduras.calet.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.altechonduras.calet.R;
import com.altechonduras.calet.Utilities;
import com.altechonduras.calet.objects.LPU;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Nery Ortez on 27-Sep-17.
 */

public class DialogLPU extends AlertDialog {
    private final EditText rda;
    private final EditText idSitio;
    private final EditText nombreSitio;
    private final EditText numeroTicket;
    private final EditText descripcion;
    private final EditText fecha;
    private final EditText falla;
    private boolean editar = false;
    private LPU item = new LPU();
    private boolean editando = false;
    private String key;
    private final OnClickListener clikc = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            item.setTime(fecha.getText().toString());
            item.setIdSitio(idSitio.getText().toString());
            item.setNombreSitio(nombreSitio.getText().toString());
            item.setRDA(rda.getText().toString());
            item.setId(numeroTicket.getText().toString());
            item.setFalla(falla.getText().toString());
            item.setDescripcion(descripcion.getText().toString());

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Utilities.getLPUdir(getContext()));
            reference.child("sent").setValue(false);


            if (!editando) {
                reference.push().setValue(item);
            } else {
                reference.child(key).setValue(item);
            }
        }
    };
    private final OnClickListener borrar = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            new AlertDialog.Builder(getContext())
                    .setMessage("¿Está seguro de eliminar este reporte?\nÉsta operación no se puede deshacer.")
                    .setPositiveButton("Borrar", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseDatabase.getInstance().getReference(Utilities.getLPUdir(getContext())).child(key).setValue(null);
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        }
    };

    public DialogLPU(@NonNull final Context context, final LPU item, final String key, boolean edit) {
        this(context);
        this.item = item;
        this.key = key;

        editando = true;
        editar = edit;

        fecha.setText(item.getTime());
        rda.setText(item.getRDA());
        idSitio.setText(item.getIdSitio());
        nombreSitio.setText(item.getNombreSitio());
        numeroTicket.setText(item.getId());
        falla.setText(item.getFalla());
        descripcion.setText(item.getDescripcion());

        setButton(BUTTON_NEGATIVE, "Borrar", borrar);

        if(!edit) {
            setButton(BUTTON_NEUTRAL, "editar", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    new DialogLPU(context, item, key, true).show();
                }
            });
            rda.setKeyListener(null);
            idSitio.setKeyListener(null);
            nombreSitio.setKeyListener(null);
            numeroTicket.setKeyListener(null);
            descripcion.setKeyListener(null);
            fecha.setKeyListener(null);
            falla.setKeyListener(null);

            setButton(BUTTON_POSITIVE, "Compartir", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    copiar(item, getContext());
                }
            });
        }
    }
    public DialogLPU(@NonNull Context context) {
        super(context);
        setTitle(R.string.NuevoLPU);
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_lpu, null);

        setView(v);

        fecha = v.findViewById(R.id.fecha);
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        fecha.setText(f.format(Calendar.getInstance().getTime()));

        rda = v.findViewById(R.id.rda);
        idSitio = v.findViewById(R.id.id_sitio);
        nombreSitio = v.findViewById(R.id.nombre_sitio);
        numeroTicket = v.findViewById(R.id.numero_ticket);
        descripcion = v.findViewById(R.id.descripcion);
        falla = v.findViewById(R.id.falla);

        setButton(BUTTON_POSITIVE, "guardar", clikc);

        setButton(BUTTON_NEUTRAL, "cancelar", ((OnClickListener) null));
    }

    private void copiar(LPU item, Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");

        String body =
                "RDA: " + item.getRDA() +
                        "\nID de Sitio: " + item.getIdSitio() +
                        "\nNombre de Sitio: " + item.getNombreSitio() +
                        "\nNúmero de ticket: " + item.getId() +
                        "\nFalla Reportada:" + item.getFalla() +
                        "\nDescripción: " + item.getDescripcion();
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(emailIntent, "Enviar a..."));
    }

}
