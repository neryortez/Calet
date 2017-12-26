package com.altechonduras.reportes.views;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.altechonduras.reportes.R;
import com.altechonduras.reportes.dialogs.DialogReportes;
import com.altechonduras.reportes.objects.Reporte;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AdaptadorReportes extends FirebaseRecyclerAdapter<AdaptadorReportes.ViewHolder, HashMap<String, Object>> {

    private final String ref;
    private Reporte reporte;

    public AdaptadorReportes(Query query, @Nullable ArrayList<HashMap<String, Object>> items, @Nullable ArrayList<String> keys, final String ref) {
        super(query, items, keys);
        this.ref = ref;
    }

    @Override
    public AdaptadorReportes.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdaptadorReportes.ViewHolder holder, int position) {
        HashMap<String, Object> item = (getItem(position));
         holder.textViewName.setText((CharSequence) item.get(reporte.getOrder().get(0)));
        holder.textViewDescription.setText((reporte.isMostrarSegundo()) ? ((CharSequence) item.get(this.reporte.getOrder().get(1))) : "");
        holder.setItem(item);
        holder.setKey(getKeys().get(position));
    }

    public void setReporte(Reporte reporte) {
        this.reporte = reporte;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewDescription;
        Button mas;
        private Map<String, Object> item;
        private String key;

        ViewHolder(View view) {
            super(view);
            textViewName = view.findViewById(R.id.key);
            textViewDescription = view.findViewById(R.id.descripcion);
            mas = view.findViewById(R.id.mostrarMas);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DialogReportes(view.getContext(), item, (ref), key, reporte, false).show();
                }
            });
            mas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DialogReportes(view.getContext(), item, (ref), key, reporte, false).show();
                }
            });


//            view.findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//                    emailIntent.setData(Uri.parse("mailto:davidpena.calet@gmail.com"));
////                    emailIntent.setType("message/rfc822");
//
//                    emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"davidpena.calet@gmail.com", "vmatute@grupocalet.com"});
//                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reporte: " + item.getRDA());
//
//                    String body =
//                            "RDA: " + item.getRDA() +
//                                    "\nID de Sitio: " + item.getGasto() +
//                                    "\nNombre de Sitio: " + item.getNombreSitio() +
//                                    "\nNúmero de ticket: " + item.getAutorizado() +
//                                    "\nCombustible: $" + item.getCombustible() +
//                                    "\nHora Inicio: " + item.getHoraInicio() +
//                                    "\nHora Final: " + item.getHoraFinal();
//                    emailIntent.putExtra(Intent.EXTRA_TEXT   , body);
//
//                    view.getContext().startActivity(emailIntent);
//                }
//            });
        }

        public void setItem(Map item) {
            this.item = item;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
}
