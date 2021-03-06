package com.altechonduras.calet.views;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.altechonduras.calet.R;
import com.altechonduras.calet.dialogs.DialogGasto;
import com.altechonduras.calet.objects.Gasto;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class AdaptadorGasto extends FirebaseRecyclerAdapter<AdaptadorGasto.ViewHolder, Gasto> {
    public AdaptadorGasto(Query query, @Nullable ArrayList<Gasto> items, @Nullable ArrayList<String> keys) {
        super(query, items, keys);
    }

    @Override
    public AdaptadorGasto.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdaptadorGasto.ViewHolder holder, int position) {
        Gasto item = getItem(position);
        holder.textViewDescription.setText(item.getTime());
        holder.textViewName.setText(item.getNombreSitio());
        holder.setItem(item);
        holder.setKey(getKeys().get(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final Button mas;
        TextView textViewName;
        TextView textViewDescription;
        private Gasto item;
        private String key;

        ViewHolder(View view) {
            super(view);
            textViewName = view.findViewById(R.id.name);
            textViewDescription = view.findViewById(R.id.descripcion);

            mas = view.findViewById(R.id.mostrarMas);
            mas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DialogGasto(view.getContext(), item, key, false).show();
                }
            });

//            view.findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//                    emailIntent.setData(Uri.parse("mailto:davidpena.calet@gmail.com"));
//
//                    emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"davidpena.calet@gmail.com", "vmatute@grupocalet.com"});
//                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reporte: " + item.getRDA());
//
//                    String body =
//                            "RDA: " + item.getRDA() +
//                                    "\nID de Sitio: " + item.getGasto() +
//                                    "\nNombre de Sitio: " + item.getNombreSitio() +
//                                    "\nNúmero de ticket: " + item.getAutorizado() +
//                                    "\nDescripción: " + item.getDescripcion();
//                    emailIntent.putExtra(Intent.EXTRA_TEXT   , body);
//
//                    view.getContext().startActivity(emailIntent);
//                }
//            });
        }

        void setItem(Gasto item) {
            this.item = item;
        }

        void setKey(String key) {
            this.key = key;
        }
    }

}
