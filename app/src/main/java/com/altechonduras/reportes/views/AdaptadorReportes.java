package com.altechonduras.reportes.views;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.altechonduras.reportes.R;
import com.altechonduras.reportes.Utilities;
import com.altechonduras.reportes.activities.ReporteDetalleActivity;
import com.altechonduras.reportes.objects.Reporte;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.altechonduras.reportes.activities.ReporteDetalleActivity.EDIT;
import static com.altechonduras.reportes.activities.ReporteDetalleActivity.ITEM_KEY;
import static com.altechonduras.reportes.activities.ReporteDetalleActivity.REFERENCE_REPORTE_DATA;
import static com.altechonduras.reportes.activities.ReporteDetalleActivity.REFERENCE_REPORTE_FORMAT;


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
        try {
            holder.textViewName.setText((CharSequence) item.get(reporte.getOrder().get(0)));
        } catch (Exception e) {
            holder.textViewName.setText("...");
            Log.d("Error en primer", "called with: position = [" + position + "]");
        }
        try {
            holder.textViewDescription.setText((reporte.isMostrarSegundo()) ? ((CharSequence) item.get(this.reporte.getOrder().get(1))) : "");
        } catch (Exception e) {
            holder.textViewDescription.setText("...");
            Log.d("Error en segundo", "called with: position = [" + position + "]");
        }
        holder.setItem(item);
        holder.setKey(getKeys().get(position));
    }

    public void setReporte(Reporte reporte) {
        this.reporte = reporte;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewDescription;
        private Map<String, Object> item;
        private String key;

        ViewHolder(View view) {
            super(view);
            textViewName = view.findViewById(R.id.key);
            textViewDescription = view.findViewById(R.id.descripcion);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent i = new Intent(context, ReporteDetalleActivity.class);
                    i.putExtra(REFERENCE_REPORTE_FORMAT, Utilities.getFormatoReporte(reporte.getId(), context));
                    i.putExtra(REFERENCE_REPORTE_DATA, ref);
                    i.putExtra(ITEM_KEY, key);
                    i.putExtra(EDIT, false);
                    context.startActivity(i);
                }
            });
        }

        public void setItem(Map item) {
            this.item = item;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
}
