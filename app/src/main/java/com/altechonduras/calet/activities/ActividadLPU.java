package com.altechonduras.calet.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.altechonduras.calet.R;
import com.altechonduras.calet.Utilities;
import com.altechonduras.calet.dialogs.DialogLPU;
import com.altechonduras.calet.objects.LPU;
import com.altechonduras.calet.views.AdaptadorLPU;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Nery Ortez on 27-Sep-17.
 */

public class ActividadLPU extends AppCompatActivity {
    private AdaptadorLPU mMyAdapter;
    private com.google.firebase.database.Query mQuery;
    private ArrayList<String> mAdapterKeys;
    private ArrayList<LPU> mAdapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_mgp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        mAdapterItems = new ArrayList<LPU>();
        mAdapterKeys = new ArrayList<String>();


        setupFirebase();
        setupRecyclerview();



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogLPU(ActividadLPU.this).show();
            }
        });

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<LPU> items = mMyAdapter.getItems();

                StringBuilder body = new StringBuilder("<html><body><p><p><table border=1>");

                for (LPU item :
                        items) {
                    body.append("<tr><td>")
                            .append(item.getTime()).append("</td><td>")
                            .append(item.getRDA()).append("</td><td>")
                            .append(item.getIdSitio()).append("</td><td>")
                            .append(item.getNombreSitio()).append("</td><td>")
                            .append(item.getId()).append("</td><td>")
                            .append(item.getDescripcion()).append("</td></tr>");
                }
                body.append("</table></body></html>");
//                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//                emailIntent.setData(Uri.parse("mailto:davidpena.calet@gmail.com"));
//
//                emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"davidpena.calet@gmail.com", "vmatute@grupocalet.com"});
//                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reportes LPU " /*+ item.getRDA()*/);
//
//                emailIntent.putExtra(Intent.EXTRA_TEXT   , Html.fromHtml(body.toString()));

//                view.getContext().startActivity(emailIntent);

                FileOutputStream fos = null;
                try {
//                    fos = openFileOutput("tempFile.html", view.getContext().MODE_MODE_WORLD_READABLE);
                    fos = new FileOutputStream("tempFile.html");
                    fos.write(body.toString().getBytes(),0,body.toString().getBytes().length);
                    fos.flush();
                    fos.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                finally {
                    if (fos != null)try {fos.close();} catch (IOException ie) {ie.printStackTrace();}
                }
                File tempFBDataFile  = new File(getFilesDir(),"tempFile.html");

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/html");
//                i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body.toString()));
                i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFBDataFile));
                view.getContext().startActivity(Intent.createChooser(i, "Eviar..."));

                BufferedWriter bw = null;
                File tempData = new File(getFilesDir(),"tempFile");
                if (tempData.exists()) {
                    tempData.delete();
                }
            }
        });
    }

    private void setupFirebase() {
        String firebaseLocation = Utilities.getLPUdir(getApplicationContext());
        mQuery = FirebaseDatabase.getInstance().getReference(firebaseLocation);
        mQuery.keepSynced(true);
    }

    private void setupRecyclerview() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        mMyAdapter = new AdaptadorLPU(mQuery, mAdapterItems, mAdapterKeys);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mMyAdapter);
    }

}
