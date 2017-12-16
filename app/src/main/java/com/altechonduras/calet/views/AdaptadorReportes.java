package com.altechonduras.calet.views;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.altechonduras.calet.R;
import com.altechonduras.calet.dialogs.DialogReportes;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Map;


public class AdaptadorReportes extends FirebaseRecyclerAdapter<AdaptadorReportes.ViewHolder, Map<String, Object>> {

    private final Map<String, Object> mformat;

    public AdaptadorReportes(Query query, @Nullable ArrayList<Map<String, Object>> items, @Nullable ArrayList<String> keys, Map<String, Object> format) {
        super(query, items, keys);
        mformat = format;
    }

    @Override
    public AdaptadorReportes.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdaptadorReportes.ViewHolder holder, int position) {
        Map<String, Object> item = getItem(position);
        holder.textViewName.setText(item.get("name").toString());
        holder.textViewDescription.setText(item.get("date").toString());
        holder.setItem(item);
        holder.setKey(getKeys().get(position));
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
            mas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DialogReportes(view.getContext(), item, key, mformat, false).show();
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
