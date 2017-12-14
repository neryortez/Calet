package com.altechonduras.calet.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.altechonduras.calet.R;
import com.altechonduras.calet.Utilities;
import com.altechonduras.calet.dialogs.DialogMGP;
import com.altechonduras.calet.objects.MGP;
import com.altechonduras.calet.views.AdaptadorMGP;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ActividadMGP extends AppCompatActivity {

    private AdaptadorMGP mMyAdapter;
    private com.google.firebase.database.Query mQuery;
    private ArrayList<String> mAdapterKeys;
    private ArrayList<MGP> mAdapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_mgp);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mAdapterItems = new ArrayList<>();
        mAdapterKeys = new ArrayList<>();


        setupFirebase();
        setupRecyclerview();



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogMGP(ActividadMGP.this).show();
            }
        });

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    System.out.println("checking permissions...");
                    // ---------------------------------- PERMISOS READ EXTERNAL ---------------------------------------------
                    if (checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 139);
                        return;
                    }
                    // ---------------------------------- PERMISOS WRITE EXTERNAL ---------------------------------------------
                    else if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 138);
                        return;

                    } else {
                        enviarPorCorreo(view);
                    }
                } else {
                    enviarPorCorreo(view);
                }
            }

            private void enviarPorCorreo(View view) {
                ArrayList<MGP> items = mMyAdapter.getItems();

                StringBuilder body = new StringBuilder("<html><body><br><table border=1>");

                for (MGP item :
                        items) {
                    body.append("<tr><td>")
                            .append(item.getFecha()).append("</td><td>")
                            .append(item.getFecha()).append("</td><td>")
                            .append(item.getIdSitio()).append("</td><td>")
                            .append(item.getNombreSitio()).append("</td><td>")
                            .append(item.getRDA()).append("</td><td>")
                            .append(item.getId()).append("</td><td>")
                            .append(item.getHoraInicio()).append("</td><td>")
                            .append(item.getHoraFinal()).append("</td><td>")
                            .append(item.getCombustible()).append("</td><td>")
                            .append(item.getGastoAcarreo()).append("</td><td>")
                            .append(item.getComentarios()).append("</td></tr>");
                }
                body.append("</table></body></html>");

                FileOutputStream fos = null;
                File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + "abc.html");
                try {
                    fos = new FileOutputStream(file.getAbsolutePath(), false);
                    fos.write(body.toString().getBytes(), 0, body.toString().getBytes().length);
                    fos.flush();
                    fos.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } finally {
                    if (fos != null) try {
                        fos.close();
                    } catch (IOException ie) {
                        ie.printStackTrace();
                    }
                }
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:davidpena.calet@gmail.com"));

                String[] emails = {"gladismarquez2015@gmail.com", "evmatute@grupocalet.com", "jorgesilva161@gmail.com"};
                emailIntent.putExtra(Intent.EXTRA_EMAIL  , emails);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reportes MGP " /*+ item.getRDA()*/);

                emailIntent.putExtra(Intent.EXTRA_TEXT   , "Adjunto encontrará el archivo con los reportes\n\n--Realizado con G-CALET REPORTES\n\nALTECH");
                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

//                view.getContext().startActivity(emailIntent);

                Intent i = new Intent(Intent.ACTION_SEND);
//                        i.setData(Uri.parse("mailto:"));
                i.setType("text/html");
//                i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body.toString()));
                i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                i.putExtra(Intent.EXTRA_EMAIL, emails);
                i.putExtra(Intent.EXTRA_SUBJECT, "Reportes MGP " /*+ item.getRDA()*/);
                view.getContext().startActivity(Intent.createChooser(i, "Enviar por correo..."));

                ((DatabaseReference) mMyAdapter.getQuery()).child("sent").setValue(true);

                BufferedWriter bw = null;
                File tempData = new File(getFilesDir(), "tempFile");
                if (tempData.exists()) {
                    tempData.delete();
                }
            }
        });
    }

    private void setupFirebase() {
        String firebaseLocation = Utilities.getMGPdir(getApplicationContext());
        mQuery = FirebaseDatabase.getInstance().getReference(firebaseLocation);
        mQuery.keepSynced(true);
    }

    private void setupRecyclerview() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        mMyAdapter = new AdaptadorMGP(mQuery, mAdapterItems, mAdapterKeys);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mMyAdapter);
    }


}
