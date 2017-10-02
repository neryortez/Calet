package com.altechonduras.calet.views;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.altechonduras.calet.R;
import com.altechonduras.calet.dialogs.DialogMGP;
import com.altechonduras.calet.objects.MGP;
import com.google.firebase.database.Query;

import java.util.ArrayList;


public class AdaptadorMGP extends FirebaseRecyclerAdapter<AdaptadorMGP.ViewHolder, MGP> {

    public AdaptadorMGP(Query query, @Nullable ArrayList<MGP> items, @Nullable ArrayList<String> keys) {
        super(query, items, keys);
    }

    @Override
    public AdaptadorMGP.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdaptadorMGP.ViewHolder holder, int position) {
        MGP item = getItem(position);
        holder.textViewName.setText(item.getNombreSitio());
        holder.textViewDescription.setText(item.getFecha());
        holder.setItem(item);
        holder.setKey(getKeys().get(position));
    }
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewDescription;
        Button mas;
        private MGP item;
        private String key;

        ViewHolder(View view) {
            super(view);
            textViewName = view.findViewById(R.id.name);
            textViewDescription = view.findViewById(R.id.descripcion);
            mas = view.findViewById(R.id.mostrarMas);
            mas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DialogMGP(view.getContext(), item, key, false).show();
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
//                                    "\nID de Sitio: " + item.getIdSitio() +
//                                    "\nNombre de Sitio: " + item.getNombreSitio() +
//                                    "\nNúmero de ticket: " + item.getId() +
//                                    "\nCombustible: $" + item.getCombustible() +
//                                    "\nHora Inicio: " + item.getHoraInicio() +
//                                    "\nHora Final: " + item.getHoraFinal();
//                    emailIntent.putExtra(Intent.EXTRA_TEXT   , body);
//
//                    view.getContext().startActivity(emailIntent);
//                }
//            });
        }

        public void setItem(MGP item) {
            this.item = item;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
}
