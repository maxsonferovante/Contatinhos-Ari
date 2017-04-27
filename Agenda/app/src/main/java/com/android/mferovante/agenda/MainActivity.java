package com.android.mferovante.agenda;

import android.Manifest;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.mferovante.agenda.adapter.ContatosAdapter;
import com.android.mferovante.agenda.database.ContatoDatabase;
import com.android.mferovante.agenda.modelo.Contato;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.AbstractQueuedLongSynchronizer;

import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends AppCompatActivity{
    private Contato aux = null;

    private static final int REQUEST_PERMISSIONS_CODE = 128;
    private static final String TAG = "LOG";
    private List<Contato> contatoList = new ArrayList<Contato>();
    private RecyclerView recyclerView;
    private ContatosAdapter contatosAdapter;
    private ContatoDatabase contatoDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ContatoActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        setRecyclerView();

        recyclerView.addOnItemTouchListener(new RecyclearTouchListener(getApplicationContext(),
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
                        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, itens);

                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                        Toast.makeText(getApplicationContext(),
                                "Novas funcionabilidades serão entreges futuramente!",
                                Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void setRecyclerView() {
        contatoDatabase = new ContatoDatabase(this);

        contatoList = contatoDatabase.buscaContatos();

        contatosAdapter = new ContatosAdapter(contatoList);

        recyclerView = (RecyclerView) findViewById(R.id.recycle_list_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(contatosAdapter);
    }

    public void onMenuItemClickAlertDialog(int itemSelected, final Contato contato, final int positionContact) {
        switch (itemSelected) {
            case 0:
                aux = contato;
                callAccessPhone();
            break;
            case 1:
                Intent intent = new Intent(MainActivity.this, ContatoActivity.class);
                intent.putExtra("contato", contato);
                startActivity(intent);
                break;
            case 2:
                new AlertDialog.Builder(this)
                        .setMessage(R.string.deseja_apagar)
                        .setCancelable(false)
                        .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                contatoDatabase = new ContatoDatabase(getApplicationContext());
                                contatoDatabase.deletar(contato);
                                setRecyclerView();
                            }
                        })
                        .setNegativeButton(R.string.nao, null).show();
                break;
            case 3:
                Toast.makeText(this, "Matriculado Clicked", Toast.LENGTH_SHORT).show();
                break;
        }

    }
    private  void callAccessPhone(){
        if( ContextCompat.checkSelfPermission( this, Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED ){

            if( ActivityCompat.shouldShowRequestPermissionRationale( this, Manifest.permission.CALL_PHONE ) ){
                callDialog( "Precisamos do acesso ao Telefone para efetuares contato com os canditatos.", new String[]{Manifest.permission.CALL_PHONE} );
            }
            else{
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSIONS_CODE );
            }
        }
        else{
            callPhone();
        }
    }
    private void callDialog( String message, final String[] permissions ){
        final MaterialDialog mMaterialDialog = new MaterialDialog(this);

        mMaterialDialog.setTitle("Permission");
        mMaterialDialog.setMessage(message);

        mMaterialDialog.setPositiveButton("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_PERMISSIONS_CODE);
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            startActivity(call);
        } else {
            Log.i("PERMISSION", "FAIL");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.i(TAG, "test");
        switch( requestCode ){
            case REQUEST_PERMISSIONS_CODE:
                for( int i = 0; i < permissions.length; i++ ){

                    if( permissions[i].equalsIgnoreCase( Manifest.permission.CALL_PHONE)
                            && grantResults[i] == PackageManager.PERMISSION_GRANTED ){

                        callPhone();
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
    @Override
    public void onBackPressed() {
        finish();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }


}
