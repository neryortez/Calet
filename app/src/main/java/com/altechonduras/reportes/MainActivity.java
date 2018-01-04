package com.altechonduras.reportes;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.altechonduras.reportes.activities.ActividadReportes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String REPORTE_ID = "reporte_id";
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private HashMap<String, Object> reportes;
    private LinearLayout botones;
    public static final GenericTypeIndicator<HashMap<String, Object>> genericTypeIndicator = new GenericTypeIndicator<HashMap<String, Object>>() {};

    private View.OnClickListener delete = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setMessage("Por favor confirme nuevamente")
                                    .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            for (Object reporte : reportes.values()) {
                                                String location = Utilities.getReportData((String) ((HashMap) reporte).get("id"),
                                                        MainActivity.this);
                                                FirebaseDatabase.getInstance().getReference(location).setValue(null);
                                            }

                                        }
                                    })
                                    .setNegativeButton("Cancelar", null)
                                    .setTitle("Alerta!")
                                    .show();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .setTitle("Alerta!")
                    .setMessage("La siguiente operación eliminará TODOS los reportes guardados, tanto de LPU como de Reporte." +
                            "\nEsta operación no se puede deshacer.")
                    .create();
            dialog.show();


//            mDatabase.getReference(Utilities.getGroup(MainActivity.this) + "/data/" + Utilities.getUserUid()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                   HashMap<String, HashMap<String, Object>> data = (HashMap<String, HashMap<String, Object>>) dataSnapshot.getValue();
//                    for (HashMap<String, Object> reporte : data.values()) {
//                        if (reporte != null) {
//                            if (reporte.get("sent") != null){
//                                if(((boolean) reporte.get("sent"))){
//                                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
//                                                            .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
//                                                                @Override
//                                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                                    new AlertDialog.Builder(MainActivity.this)
//                                                                            .setMessage("Por favor confirme nuevamente")
//                                                                            .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
//                                                                                @Override
//                                                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                                                    String firebaseLocation = Utilities.getLPUdir(getApplicationContext());
//                                                                                    FirebaseDatabase.getInstance().getReference(firebaseLocation).setValue(null);
//                                                                                    firebaseLocation = Utilities.getMGPdir(getApplicationContext());
//                                                                                    FirebaseDatabase.getInstance().getReference(firebaseLocation).setValue(null);
//                                                                                }
//                                                                            })
//                                                                            .setNegativeButton("Cancelar", null)
//                                                                            .setTitle("Alerta!")
//                                                                            .show();
//                                                                }
//                                                            })
//                                                            .setNegativeButton("Cancelar", null)
//                                                            .setTitle("Alerta!")
//                                                            .setMessage("La siguiente operación eliminará TODOS los reportes guardados, tanto de LPU como de Reporte." +
//                                                                    "\nEsta operación no se puede deshacer.")
//                                                            .create();
//                                                    dialog.show();
//                                }
//                            }
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });

//            mDatabase.getReference(Utilities.getLPUdir(MainActivity.this)).child("sent").addListenerForSingleValueEvent(
//                    new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            if (!dataSnapshot.exists()) {
//                                showVallaAEnviar();
//                                return;
//                            }
//                            if (dataSnapshot.getValue(boolean.class)) {
//                                mDatabase.getReference(Utilities.getMGPdir(MainActivity.this)).child("sent").addListenerForSingleValueEvent(
//                                        new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                                if (!dataSnapshot.exists()) {
//                                                    showVallaAEnviar();
//                                                    return;
//                                                }
//                                                if (dataSnapshot.getValue(boolean.class)) {
//                                                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
//                                                            .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
//                                                                @Override
//                                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                                    new AlertDialog.Builder(MainActivity.this)
//                                                                            .setMessage("Por favor confirme nuevamente")
//                                                                            .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
//                                                                                @Override
//                                                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                                                    String firebaseLocation = Utilities.getLPUdir(getApplicationContext());
//                                                                                    FirebaseDatabase.getInstance().getReference(firebaseLocation).setValue(null);
//                                                                                    firebaseLocation = Utilities.getMGPdir(getApplicationContext());
//                                                                                    FirebaseDatabase.getInstance().getReference(firebaseLocation).setValue(null);
//                                                                                }
//                                                                            })
//                                                                            .setNegativeButton("Cancelar", null)
//                                                                            .setTitle("Alerta!")
//                                                                            .show();
//                                                                }
//                                                            })
//                                                            .setNegativeButton("Cancelar", null)
//                                                            .setTitle("Alerta!")
//                                                            .setMessage("La siguiente operación eliminará TODOS los reportes guardados, tanto de LPU como de Reporte." +
//                                                                    "\nEsta operación no se puede deshacer.")
//                                                            .create();
//                                                    dialog.show();
//
//                                                } else {
//                                                    showVallaAEnviar();
//                                                }
//                                            } @Override public void onCancelled(DatabaseError databaseError) {}
//                                        }
//                                );
//                            } else {
//                                showVallaAEnviar();
//                            }
//                        } @Override public void onCancelled(DatabaseError databaseError) {}
//                    }
//            );
        }
    };
    private String grupo;

    @NonNull
    private ValueEventListener mListenerDeReportes = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                reportes = dataSnapshot.getValue(genericTypeIndicator);
                if (reportes == null) {
                    //TODO: HAcer algo si la lista de reportes esta vacia...
                    reportes = new HashMap<>();
                    Map<String, Object> reporte1 = new HashMap<>();
                    reporte1.put("name", "Reporte Demo");
                    Map<String, Object> format = new HashMap<>();
                    format.put("RDA", "type_string");
                    reporte1.put("format", format);
                    reportes.put("reporte1", reporte1);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        botones.removeAllViews();
                        for (final Object reporte : reportes.values()) {
                            final Button boton = new Button(MainActivity.this);
                            boton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getApplicationContext(), ActividadReportes.class);
                                    intent.putExtra(REPORTE_ID, ((String) ((Map<String, Object>)reporte).get("id")));
                                    startActivity(intent);
                                }
                            });
                            boton.setText(((Map<String, Object>) reporte).get("name").toString());
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    botones.addView(boton);
                                }
                            });
                        }
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        grupo = Utilities.getGrupo(MainActivity.this);
        ((TextView) findViewById(R.id.textView)).setText("Bienvenido a la APP para Reportes de: " + grupo);

        botones = findViewById(R.id.lista_botones);
        DatabaseReference reportes = mDatabase.getReference().child(grupo)
                .child("reportes");
        ValueEventListener valueEventListener = reportes.addValueEventListener(
                mListenerDeReportes);

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("¿Esta seguro de cerrar esta sesión?")
                        .setPositiveButton("Cerrar Sesión", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase.getInstance()
                                        .getReference(Utilities.getGroup(MainActivity.this)
                                                + "/users/"
                                                + mAuth.getCurrentUser().getUid()
                                        )
                                        .child("device").setValue(null)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                System.exit(0);
                                            }
                                        });
                                FirebaseAuth.getInstance().signOut();
                            }
                        }).setNegativeButton("Cancelar", null).show();
            }
        });



        findViewById(R.id.delete).setOnClickListener(delete);

        findViewById(R.id.altech).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://www.altechonduras.com/sobre-nosotros/";
                Uri webpage = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        ((TextView) findViewById(R.id.user)).setText("Usuario: " + mAuth.getCurrentUser().getEmail());

//        new LoadImage((ImageView) findViewById(R.id.imageView))
//                .execute("https://firebasestorage.googleapis.com/v0/b/calet-dc66e.appspot.com/o/22548619_729583130575410_1715317472784384981_o%20(1).jpg?alt=media&token=c614227b-73a1-4c6b-825e-52ac8b8c68ad");
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void showVallaAEnviar() {
        new AlertDialog.Builder(this)
                .setMessage("Favor envíe los reportes Reporte y LPU para poder realizar esta operación.")
                .show();
    }

    private void enviarPorCorreo(View view) {
//        ArrayList<LPU> items = null/*mMyAdapter.getItems()*/;
//
//        StringBuilder body = new StringBuilder("<html><body><br><table border=1>");
//
//        for (LPU item :
//                items) {
//            body.append("<tr><td>")
//                    .append(item.getTime()).append("</td><td>")
//                    .append(item.getNombreSitio()).append("</td><td>")
//                    .append(item.getIdSitio()).append("</td><td>")
//                    .append(item.getRDA()).append("</td><td>")
//                    .append(item.getId()).append("</td><td>")
//                    .append(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).append("</td><td>")
//                    .append(item.getFalla()).append("</td><td>")
//                    .append(item.getDescripcion()).append("</td></tr>");
//        }
//        body.append("</table></body></html>");
//
//        FileOutputStream fos = null;
//        File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + "abc.html");
//        try {
//            fos = new FileOutputStream(file.getAbsolutePath(), false);
//            fos.write(body.toString().getBytes(), 0, body.toString().getBytes().length);
//            fos.flush();
//            fos.close();
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        } finally {
//            if (fos != null) try {
//                fos.close();
//            } catch (IOException ie) {
//                ie.printStackTrace();
//            }
//        }
//        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//        emailIntent.setData(Uri.parse("mailto:davidpena.calet@gmail.com"));
//
//        String[] emails = {"davidpena.calet@gmail.com", "vmatute@grupocalet.com"};
//        emailIntent.putExtra(Intent.EXTRA_EMAIL  , emails);
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reportes LPU " /*+ item.getRDA()*/);
//
//        emailIntent.putExtra(Intent.EXTRA_TEXT   , "Adjunto encontrará el archivo con los reportes\n\n--Realizado con G-CALET REPORTES\n\nALTECH");
//        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//
////                view.getContext().startActivity(emailIntent);
//
//        Intent i = new Intent(Intent.ACTION_SEND);
////                        i.setData(Uri.parse("mailto:"));
//        i.setType("text/html");
////                i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body.toString()));
//        i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//        i.putExtra(Intent.EXTRA_EMAIL, emails);
//        i.putExtra(Intent.EXTRA_SUBJECT, "Reportes LPU " /*+ item.getRDA()*/);
//        view.getContext().startActivity(Intent.createChooser(i, "Enviar por correo..."));
//
//        BufferedWriter bw = null;
//        File tempData = new File(getFilesDir(), "tempFile");
//        if (tempData.exists()) {
//            tempData.delete();
//        }
    }

    // show The Image in a ImageView


//    public void onClick(View v) {
//        startActivity(new Intent(this, IndexActivity.class));
//        finish();
//
//    }

    class LoadImage extends AsyncTask<String, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewReference;

        public LoadImage(ImageView imageView) {
            imageViewReference = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                return downloadBitmap(params[0]);
            } catch (Exception e) {
                Log.e("LoadImage class", "doInBackground() " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
        }

        private Bitmap downloadBitmap(String url) {
            HttpURLConnection urlConnection = null;
            try {
                Log.e("LoadImage class", "Descargando imagen desde url: " + url);
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();
                int statusCode = urlConnection.getResponseCode();
                if (statusCode != HttpURLConnection.HTTP_OK) {
                    Log.e("LoadImage class", "No paso nada: " + statusCode);
                    return null;
                }

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Log.e("LoadImage class", "InpuStream no nulo");
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }
            } catch (Exception e) {
                urlConnection.disconnect();
                Log.e("LoadImage class", "Error en url: " + url);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }

}
