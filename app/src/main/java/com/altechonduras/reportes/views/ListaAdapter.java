package com.altechonduras.reportes.views;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.altechonduras.reportes.R;

import java.util.ArrayList;
import java.util.HashMap;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by Nery Ortez on 27-Dec-17.
 */

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ViewHolder> {

    private final Context context;
    private final String title;
    private ArrayList<HashMap<String, String>> lista;
    private final ArrayList<String> campos;

    public ListaAdapter(ArrayList<HashMap<String, String>> lista, ArrayList<String> campos, Context context, String title) {
        this.context = context;
        this.lista = lista != null ? lista : new ArrayList<HashMap<String, String>>(0);
        this.campos = campos;
        this.title = title;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_item, parent, false);
        return new ViewHolder(view, campos);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setItem(getItem(position));
    }

    private HashMap<String, String> getItem(int position) {
        return lista.get(position);
    }

    @Override
    public int getItemCount() {
        if (lista == null) lista = new ArrayList<>();
        return lista.size();
    }

    public void setLista(ArrayList<HashMap<String, String>> lista) {
        this.lista = lista;
        notifyDataSetChanged();
    }

    public void addNew() {
        addOrEditNew(null, -1);
    }

    private void addOrEditNew(final HashMap<String, String> item, final int index) {
        final HashMap<String, String> e = new HashMap<>();
        if (item != null) {
            e.putAll(item);
        }

        LinearLayout dialogLayout = new LinearLayout(context);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        final HashMap<String, EditText> camposMap = new HashMap<>();

        for (String campo : campos) {
            EditText edit = new EditText(context);
            edit.setHint(campo);
            dialogLayout.addView(edit);
            camposMap.put(campo, edit);
            if (e.size() > 0) edit.setText(e.get(campo));

        }
        new AlertDialog.Builder(context)
                .setView(dialogLayout)
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (String key : camposMap.keySet()) {
                            e.put(key, camposMap.get(key).getText().toString());
                        }
                        if (item == null) {
                            lista.add(e);
                            notifyItemInserted(lista.indexOf(e));
                        } else {
                            lista.set(index, e);
                            notifyItemChanged(index);
                        }
                    }
                }).setNegativeButton(item == null ? "Cancelar" : "Borrar", item == null ? null : new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        lista.remove(index);
                        notifyItemRemoved(index);
                    }
                })
                .setTitle(title)
                .show();
    }

    public void addNew(HashMap<String, String> ob) {
        this.lista.add(ob);
        this.notifyItemInserted(this.lista.indexOf(ob));
    }

    public ArrayList<HashMap<String, String>> getLista() {
        return lista;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        private HashMap<String, TextView> viewMap = new HashMap<>();

        ViewHolder(View itemView, ArrayList<String> campos) {
            super(itemView);
            this.view = itemView;
            for (String campo : campos) {
                TextView t = new TextView(itemView.getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                params.setMargins(10,10,10,10);
                t.setLayoutParams(params);
                viewMap.put(campo, t);
                ((LinearLayout) itemView).addView(t);
            }
        }

        public void setItem(final HashMap<String, String> item) {
            for (String campo : viewMap.keySet()) {
                viewMap.get(campo).setText(item.get(campo));
            }
            this.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addOrEditNew(item, getAdapterPosition());
                }
            });
        }
    }
}
