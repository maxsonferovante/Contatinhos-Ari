package com.android.mferovante.agenda.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.mferovante.agenda.ContatoActivity;
import com.android.mferovante.agenda.MainActivity;
import com.android.mferovante.agenda.R;
import com.android.mferovante.agenda.RecyclearTouchListener;
import com.android.mferovante.agenda.adapter.ContatosAdapter;
import com.android.mferovante.agenda.database.ContatoDatabase;
import com.android.mferovante.agenda.modelo.Contato;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by mferovante on 15/05/17.
 */

public class CanditatosFragment extends Fragment {
    private Contato aux = null;

    private View rootView = null;
    private List<Contato> contatoList = new ArrayList<Contato>();
    private RecyclerView recyclerView;
    private ContatosAdapter contatosAdapter;
    private ContatoDatabase contatoDatabase;

    public CanditatosFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public void onResume(){
        super.onResume();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_list_view_canditatos);
        setRecyclerView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (this.rootView == null){
            this.rootView = inflater.inflate(R.layout.canditatos_fragment, container, false);
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_list_view_canditatos);
            setRecyclerView();
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view,
                              Bundle savedInstanceState) {

        recyclerView.addOnItemTouchListener(new RecyclearTouchListener(getContext(),
                recyclerView,
                new RecyclearTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, final int position) {
                        final Contato contato = contatoList.get(position);
                        //Lista de itens
                        ArrayList<String> itens = new ArrayList<String>();
                        itens.add("Efetuar ligação");
                        itens.add("Editação dos dados");
                        itens.add("Excluir contato");
                        itens.add("Matriculado");

                        //adapter utilizando um layout customizado (TextView)
                        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, itens);

                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Opções");

                        //define o diálogo como uma lista, passa o adapter.
                        builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();
                                onMenuItemClickAlertDialog(arg1, contato, position);
                            }
                        });

                        builder.create().show();
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        Contato contato = contatoList.get(position);
                        Toast.makeText(getContext(),
                                "Novas funcionabilidades serão entreges futuramente!",
                                Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(MainActivity.TAG, "test");
        switch( requestCode ){
            case MainActivity.REQUEST_PERMISSIONS_CODE:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    callPhone();
                    Log.i(MainActivity.TAG, "TELEFONE permission has now been granted.");
                }
                else {
                    Log.i(MainActivity.TAG, "TELEFONE permission was NOT granted.");
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onMenuItemClickAlertDialog(int itemSelected, final Contato contato, final int positionContact) {
        switch (itemSelected) {
            case 0:
                aux = contato;
                callPhone();
                break;
            case 1:
                Intent intent = new Intent(getActivity(), ContatoActivity.class);
                intent.putExtra("contato", contato);
                startActivity(intent);
                break;
            case 2:
                new AlertDialog.Builder(getContext())
                        .setMessage(R.string.deseja_apagar)
                        .setCancelable(false)
                        .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                contatoDatabase = new ContatoDatabase(getContext());
                                contatoDatabase.deletar(contato);
                                updateDataSet();
                            }
                        })
                        .setNegativeButton(R.string.nao, null).show();
                break;
            case 3:
                new AlertDialog.Builder(getContext())
                        .setMessage(R.string.deseja_alterar_para_matriculado)
                        .setCancelable(false)
                        .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                contatoDatabase = new ContatoDatabase(getContext());
                                contato.setMatriculado(1);
                                contatoDatabase.alterar(contato);
                                updateDataSet();
                            }
                        })
                        .setNegativeButton(R.string.nao, null).show();
                break;
        }
    }

    private  void callAccessPhone(){
        if( ActivityCompat.shouldShowRequestPermissionRationale( getActivity(), Manifest.permission.CALL_PHONE ) ){

            callDialog( "Precisamos do acesso ao Telefone para efetuares contato com os canditatos.", new String[]{Manifest.permission.CALL_PHONE} );
        }
        else{
            ActivityCompat.requestPermissions( getActivity(), new String[]{Manifest.permission.CALL_PHONE}, MainActivity.REQUEST_PERMISSIONS_CODE );
        }
    }
    private void callDialog( String message, final String[] permissions ){
        final MaterialDialog mMaterialDialog = new MaterialDialog(getContext());

        mMaterialDialog.setTitle("Permission");
        mMaterialDialog.setMessage(message);

        mMaterialDialog.setPositiveButton("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityCompat.requestPermissions(getActivity(), permissions, MainActivity.REQUEST_PERMISSIONS_CODE);
                mMaterialDialog.dismiss();
            }
        })
                .setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();
    }
    private void callPhone(){
        Intent call = new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:" + aux.getTelefone()
                )
        );
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            callAccessPhone();
        } else {
            startActivity(call);
        }
    }
    private void setRecyclerView() {
        contatoDatabase = new ContatoDatabase(getContext());

        //Consultando todos os canditatos, por isso o args 0
        contatoList = contatoDatabase.buscaContatos(0);
        contatosAdapter = new ContatosAdapter(contatoList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(contatosAdapter);
    }

    public void updateDataSet(){
        contatoDatabase = new ContatoDatabase(getContext());
        contatoList = contatoDatabase.buscaContatos(0);

        contatosAdapter = new ContatosAdapter(contatoList);
        recyclerView.setAdapter(contatosAdapter);
    }
}
