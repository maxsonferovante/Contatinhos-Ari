package com.android.mferovante.agenda;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mferovante.agenda.database.ContatoDatabase;
import com.android.mferovante.agenda.modelo.Contato;

/**
 * Created by mferovante on 02/03/17.
 */

public class ContatoActivity extends AppCompatActivity {
    private EditText contato_edtNome, contato_edtinfo, contato_edtTelefone;
    private TextView contato_textId;
    private Button contato_btnSalvar;
    private LinearLayout activity_contato;
    private ProgressBar contato_progressBar;
    private String id, nome, info, telefone;

    private Contato contato;
    private ContatoDatabase contatoDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);

        findView();

        Intent intent = getIntent();
        contato = (Contato) intent.getSerializableExtra("contato");
        if (contato != null) {
            contato_textId.setText(contato.getId());
            contato_edtNome.setText(contato.getNome());
            contato_edtinfo.setText(contato.getInfo());
            contato_edtTelefone.setText(contato.getTelefone());
        } else {
            contato = new Contato();
        }

        contato_btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pegarValores();
                habilitarProgress(View.VISIBLE, false);
                if (TextUtils.isEmpty(nome)) {
                    Snackbar.make(activity_contato, R.string.digite_seu_nome, Snackbar.LENGTH_LONG).show();
                    habilitarProgress(View.GONE, true);
                    return;
                }
                if (TextUtils.isEmpty(info)) {
                    Snackbar.make(activity_contato, R.string.digite_seu_info, Snackbar.LENGTH_LONG).show();
                    habilitarProgress(View.GONE, true);
                    return;
                }
                if (TextUtils.isEmpty(telefone)) {
                    Snackbar.make(activity_contato, R.string.digite_seu_telefone, Snackbar.LENGTH_LONG).show();
                    habilitarProgress(View.GONE, true);
                    return;
                }
                contato.setNome(nome);
                contato.setInfo(info);
                contato.setTelefone(telefone);
                //
                contatoDatabase = new ContatoDatabase(getApplicationContext());
                if (Integer.parseInt(contato.getId()) > 0) {
                    alterar();
                } else {
                    inserir();
                }
            }
        });

    }

    private void alterar() {
        contatoDatabase.alterar(contato);
        Toast.makeText(getApplicationContext(), R.string.salvo_com_sucesso, Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void inserir() {
        if (contatoDatabase.insere(contato) > 0) {
            Toast.makeText(getApplicationContext(), R.string.salvo_com_sucesso, Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else {
            Snackbar.make(activity_contato, R.string.nao_foi_possivel_salvar, Snackbar.LENGTH_LONG).show();
            habilitarProgress(View.GONE, true);
        }
    }

    private void habilitarProgress(int visible, boolean b) {
        contato_progressBar.setVisibility(visible);
        contato_btnSalvar.setClickable(b);
    }

    private void pegarValores() {
        id = contato_textId.getText().toString();
        nome = contato_edtNome.getText().toString();
        info = contato_edtinfo.getText().toString();
        telefone = contato_edtTelefone.getText().toString();
    }

    private void findView() {
        contato_textId = (TextView) findViewById(R.id.textViewId);
        contato_edtNome = (EditText) findViewById(R.id.contato_edtNome);
        contato_edtTelefone = (EditText) findViewById(R.id.contato_edtTelefone);
        contato_edtinfo = (EditText) findViewById(R.id.contato_edtinfo);
        contato_btnSalvar = (Button) findViewById(R.id.contato_btnSalvar);
        activity_contato = (LinearLayout) findViewById(R.id.activity_contato);
        contato_progressBar = (ProgressBar) findViewById(R.id.contato_progressBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (Integer.parseInt(contato.getId()) > 0) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_contato, menu);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.contato_ligar:
                Intent call = new Intent(Intent.ACTION_CALL,
                        Uri.parse("tel:" + contato.getTelefone()
                        )
                );
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED) {
                    startActivity(call);
                } else {
                    Log.i("PERMISSION", "FAIL");
                }
                return true;
            case R.id.contato_apagar:
                new AlertDialog.Builder(this)
                        .setMessage(R.string.deseja_apagar)
                        .setCancelable(false)
                        .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                contatoDatabase = new ContatoDatabase(getApplicationContext());
                                contatoDatabase.deletar(contato);
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.nao, null).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
