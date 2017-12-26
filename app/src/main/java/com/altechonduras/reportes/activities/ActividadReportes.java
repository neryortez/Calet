package com.altechonduras.reportes.activities;

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

import com.altechonduras.reportes.MainActivity;
import com.altechonduras.reportes.R;
import com.altechonduras.reportes.Utilities;
import com.altechonduras.reportes.dialogs.DialogReportes;
import com.altechonduras.reportes.objects.Reporte;
import com.altechonduras.reportes.views.AdaptadorReportes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActividadReportes extends AppCompatActivity {

    private AdaptadorReportes mMyAdapter;
    private Query mQuery;
    private String mReporteID;
    private ArrayList<String> orden;
    private Reporte reporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mReporteID = getIntent().getStringExtra(MainActivity.REPORTE_ID);

        setContentView(R.layout.activity_actividad_mgp);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        mAdapterItems = new ArrayList<>();
//        mAdapterKeys = new ArrayList<>();

        setupFirebase();



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogReportes(ActividadReportes.this, Utilities.getReportData(mReporteID, ActividadReportes.this), reporte).show();
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
                ArrayList<HashMap<String, Object>> items = mMyAdapter.getItems();

                StringBuilder body = new StringBuilder("<br><table border=1>");

                for (Map<String, Object> item :
                        items) {
                    body.append("<tr>");
                    for (Object s : item.keySet().toArray()) {
                        body.append("<td>").append(item.get(s)).append("</td>");
                    }
                    body.append("</tr>");
                }
                body.append("</table>");

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
                emailIntent.setData(Uri.parse(reporte.getRecipientes().get(0)/*"mailto:davidpena.calet@gmail.com"*/));

                String[] emails = {};
                emails = reporte.getRecipientes().toArray(emails);// {"gladismarquez2015@gmail.com", "evmatute@grupocalet.com", "jorgesilva161@gmail.com"};
                emailIntent.putExtra(Intent.EXTRA_EMAIL  , emails);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reportes Reporte " /*+ item.getRDA()*/);

                emailIntent.putExtra(Intent.EXTRA_TEXT   , "Adjunto encontrará el archivo con los reportes\n\n--Realizado con G-CALET REPORTES\n\nALTECH");
                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

//                view.getContext().startActivity(emailIntent);

                Intent i = new Intent(Intent.ACTION_SEND);
//                        i.setData(Uri.parse("mailto:"));
                i.setType("text/html");
//                i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body.toString()));
                i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                i.putExtra(Intent.EXTRA_EMAIL, emails);
                i.putExtra(Intent.EXTRA_SUBJECT, "Reportes Reporte " /*+ item.getRDA()*/);
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
        String firebaseLocation = "";

        firebaseLocation = Utilities.getReportData(mReporteID, this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mQuery = database.getReference(firebaseLocation);

        database.getReference(Utilities.getFormatoReporte(mReporteID, this)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reporte = dataSnapshot.getValue(Reporte.class);
//                setupRecyclerview();
                setTitle(reporte.getName() != null ? reporte.getName() : "Reporte");
                RecyclerView recyclerView = findViewById(R.id.recyclerview);
                mMyAdapter = new AdaptadorReportes(mQuery, new ArrayList<HashMap<String, Object>>(), new ArrayList<String>(), Utilities.getReportData(mReporteID, ActividadReportes.this));
                mMyAdapter.setReporte(reporte);
                recyclerView.setLayoutManager(new LinearLayoutManager(ActividadReportes.this));
                recyclerView.setAdapter(mMyAdapter);
            } @Override public void onCancelled(DatabaseError databaseError) {}
        });

        //TODO: keepSynced todo la Reference del gruop, para asi poder tener acceso sin conexion a los datos.
        mQuery.keepSynced(true); //TODO: Eliminar...?
    }

    private void setupRecyclerview() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        mMyAdapter = new AdaptadorReportes(mQuery, new ArrayList<HashMap<String, Object>>(), new ArrayList<String>(), Utilities.getReportData(mReporteID, this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mMyAdapter);
    }


}
