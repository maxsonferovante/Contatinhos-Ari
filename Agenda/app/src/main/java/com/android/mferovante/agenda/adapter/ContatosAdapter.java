package com.android.mferovante.agenda.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.mferovante.agenda.R;
import com.android.mferovante.agenda.modelo.Contato;

import java.util.List;

/**
 * Created by mferovante on 02/03/17.
 */

public class ContatosAdapter extends RecyclerView.Adapter<ContatosAdapter.MyViewHolder> {
    private List<Contato> contatosList;


    public ContatosAdapter(List<Contato> contatosList) {
        this.contatosList = contatosList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contato_list_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Contato contato = contatosList.get(position);

        holder.textViewId.setText(Integer.toString(position + 1));

        holder.textViewNome.setText(contato.getNome());
        holder.textViewinfo.setText(contato.getInfo());
        holder.textViewTelefone.setText(contato.getTelefone());
    }

    @Override
    public int getItemCount() {
        return contatosList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNome, textViewinfo, textViewTelefone, textViewId;
        public MyViewHolder(View itemView) {
            super(itemView);
            textViewId = (TextView) itemView.findViewById(R.id.list_id);
            textViewNome = (TextView) itemView.findViewById(R.id.list_nome);
            textViewinfo = (TextView) itemView.findViewById(R.id.list_info);
            textViewTelefone = (TextView) itemView.findViewById(R.id.list_telefone);
        }
    }
}
