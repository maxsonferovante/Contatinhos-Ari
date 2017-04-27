package com.android.mferovante.agenda;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

public class MainActivity extends AppCompatActivity{
    private List<Contato> contatoList = new ArrayList<Contato>();
    private RecyclerView recyclerView;
    private ContatosAdapter contatosAdapter;
    private ContatoDatabase contatoDatabase;
    private Contato contato;

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

        contatoDatabase = new ContatoDatabase(this);

        contatoList = contatoDatabase.buscaContatos();

        contatosAdapter = new ContatosAdapter(contatoList);

        recyclerView = (RecyclerView) findViewById(R.id.recycle_list_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(contatosAdapter);


        recyclerView.addOnItemTouchListener(new RecyclearTouchListener(getApplicationContext(),
                recyclerView,
                new RecyclearTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        /*Contato contato = contatoList.get(position);
                        Intent intent = new Intent(MainActivity.this, ContatoActivity.class);
                        intent.putExtra("contato", contato);
                        startActivity(intent);*/
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
                                onMenuItemClickAlertDialog(arg1);

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

    public void onMenuItemClickAlertDialog(int position) {
        switch (position) {
            case 0:
                Toast.makeText(this, "Ligar Clicked", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(this, "Editar Clicked", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "Apagar Clicked", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this, "Matriculado Clicked", Toast.LENGTH_SHORT).show();
                break;
        }

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
