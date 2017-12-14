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
import com.altechonduras.calet.objects.Gasto;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DialogGasto extends AlertDialog {
    private final EditText fecha;
    private final EditText rda;
    private final EditText nombreSitio;
    private final EditText gasto;
    private final EditText autorizado;
    private final EditText pagoA;
    private final EditText cedula;
    private final EditText descripcion;
    private final EditText telefono;
    private boolean editar = false;
    private Gasto item = new Gasto();
    private boolean editando = false;
    private String key;
    private final OnClickListener clikc = new OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            item.setTime(fecha.getText().toString());
            item.setRDA(rda.getText().toString());
            item.setNombreSitio(nombreSitio.getText().toString());
            item.setGasto(gasto.getText().toString());
            item.setAutorizado(autorizado.getText().toString());
            item.setPagoA(pagoA.getText().toString());
            item.setDescripcion(descripcion.getText().toString());
            item.setCedula(cedula.getText().toString());
            item.setTelefono(telefono.getText().toString());

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Utilities.getGastodir(getContext()));
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
            new Builder(getContext())
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

    public DialogGasto(@NonNull final Context context, final Gasto item, final String key, boolean edit) {
        this(context);
        this.item = item;
        this.key = key;

        editando = true;
        editar = edit;

        fecha.setText(item.getTime());
        rda.setText(item.getRDA());
        nombreSitio.setText(item.getNombreSitio());
        gasto.setText(item.getGasto());
        autorizado.setText(item.getAutorizado());
        pagoA.setText(item.getPagoA());
        cedula.setText(item.getDescripcion());
        descripcion.setText(item.getDescripcion());
        telefono.setText(item.getTelefono());

        setButton(BUTTON_NEGATIVE, "Borrar", borrar);

        if (!edit) {
            setButton(BUTTON_NEUTRAL, "editar", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    new DialogGasto(context, item, key, true).show();
                }
            });
            fecha.setKeyListener(null);
            rda.setKeyListener(null);
            nombreSitio.setKeyListener(null);
            gasto.setKeyListener(null);
            autorizado.setKeyListener(null);
            pagoA.setKeyListener(null);
            cedula.setKeyListener(null);
            descripcion.setKeyListener(null);
            telefono.setKeyListener(null);

            setButton(BUTTON_POSITIVE, "Compartir", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    copiar(item, getContext());
                }
            });
        }
    }

    public DialogGasto(@NonNull Context context) {
        super(context);
        setTitle(context.getString(R.string.nuevo_gasto));
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_gastos, null);

        setView(v);

        fecha = v.findViewById(R.id.fecha);
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        fecha.setText(f.format(Calendar.getInstance().getTime()));

        rda = v.findViewById(R.id.rda);
        nombreSitio = v.findViewById(R.id.nombre_sitio);
        gasto = v.findViewById(R.id.monto_gasto);
        autorizado = v.findViewById(R.id.autorizado);
        pagoA = v.findViewById(R.id.pago_a);
        cedula = v.findViewById(R.id.cedula);
        descripcion = v.findViewById(R.id.descripcion);
        telefono = v.findViewById(R.id.telefono);

        setButton(BUTTON_POSITIVE, "guardar", clikc);

        setButton(BUTTON_NEUTRAL, "cancelar", ((OnClickListener) null));
    }

    private void copiar(Gasto item, Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");

        String body =
                "Gasto: " + item.getRDA() +
                        "\nFecha: " + item.getTime() +
                        "\nNombre de Sitio: " + item.getNombreSitio() +
                        "\nMonto de Gasto: " + item.getGasto() +
                        "\nAutorizado por:" + item.getAutorizado() +
                        "\nSe pagó a: " + item.getPagoA() +
                        "\nNo. Cédula " + item.getCedula() +
                        "\nNo. Telefono " + item.getTelefono() +
                        "\nDescripción del Gasto: " + item.getDescripcion();
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(emailIntent, "Enviar a..."));
    }

}
