package com.android.mferovante.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.mferovante.agenda.adapter.ContatosAdapter;
import com.android.mferovante.agenda.database.ContatoDatabase;
import com.android.mferovante.agenda.modelo.Contato;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
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
                startActivity(new Intent(getApplicationContext(), ContatoActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        recyclerView = (RecyclerView) findViewById(R.id.recycle_list_view);

        contatoDatabase = new ContatoDatabase(this);

        contatoList = contatoDatabase.buscaContatos();

        contatosAdapter = new ContatosAdapter(contatoList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(contatosAdapter);


        recyclerView.addOnItemTouchListener(new RecyclearTouchListener(getApplicationContext(),
                recyclerView,
                new RecyclearTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Contato contato = contatoList.get(position);
                        Intent intent = new Intent(getApplicationContext(), ContatoActivity.class);
                        intent.putExtra("contato", contato);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        Contato contato = contatoList.get(position);
                        Toast.makeText(getApplicationContext(), contato.getTelefone(), Toast.LENGTH_LONG)
                                .show();
                    }
                }));
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

}
