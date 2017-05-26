package com.android.mferovante.agenda.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
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

import com.android.mferovante.agenda.R;
import com.android.mferovante.agenda.database.ContatoDatabase;
import com.android.mferovante.agenda.modelo.Contato;

/**s
 * Created by mferovante on 02/03/17.
 */

public class ContatoActivity extends AppCompatActivity {
    private EditText contato_edtNome, contato_edtinfo, contato_edtTelefone;
    private Button contato_btnSalvar;
    private LinearLayout activity_contato;
    private ProgressBar contato_progressBar;
    private String nome, info, telefone;

    private Contato contato;
    private ContatoDatabase contatoDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);

        findView();

        Intent intent = getIntent();
        contato = (Contato) intent.getSerializableExtra("contato");
        if (contato != null) {
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
                    Snackbar.make(activity_contato, R.string.digite_seu_nome, Snackbar.LENGTH_SHORT).show();
                    habilitarProgress(View.GONE, true);
                    return;
                }
                if (TextUtils.isEmpty(info)) {
                    Snackbar.make(activity_contato, R.string.digite_seu_info, Snackbar.LENGTH_SHORT).show();
                    habilitarProgress(View.GONE, true);
                    return;
                }
                if (TextUtils.isEmpty(telefone)) {
                    Snackbar.make(activity_contato, R.string.digite_seu_telefone, Snackbar.LENGTH_SHORT).show();
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
        Toast.makeText(getApplicationContext(), R.string.salvo_com_sucesso, Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    private void inserir() {
        if (contatoDatabase.insere(contato) > 0) {
            Toast.makeText(getApplicationContext(), R.string.salvo_com_sucesso, Toast.LENGTH_SHORT).show();
            onBackPressed();
        } else {
            Snackbar.make(activity_contato, R.string.nao_foi_possivel_salvar, Snackbar.LENGTH_SHORT).show();
            habilitarProgress(View.GONE, true);
        }
    }

    private void habilitarProgress(int visible, boolean b) {
        contato_progressBar.setVisibility(visible);
        contato_btnSalvar.setClickable(b);
    }

    private void pegarValores() {
        nome = contato_edtNome.getText().toString();
        info = contato_edtinfo.getText().toString();
        telefone = contato_edtTelefone.getText().toString();
    }

    private void findView() {
        contato_edtNome = (EditText) findViewById(R.id.contato_edtNome);
        contato_edtTelefone = (EditText) findViewById(R.id.contato_edtTelefone);
        contato_edtinfo = (EditText) findViewById(R.id.contato_edtinfo);
        contato_btnSalvar = (Button) findViewById(R.id.contato_btnSalvar);
        activity_contato = (LinearLayout) findViewById(R.id.activity_contato);
        contato_progressBar = (ProgressBar) findViewById(R.id.contato_progressBar);
    }

    @Override
    public void onBackPressed() {
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            //Se a atividade não faz parte do aplicativo, criamos uma nova tarefa
            // para navegação com a pilha de volta sintetizada.
            TaskStackBuilder.create(this)
                    // Adiciona todas atividades parentes na pilha de volta
                    .addNextIntentWithParentStack(upIntent)
                    .startActivities();
        } else {
            //Se essa atividade faz parte da tarefa do app
            //navegamos para seu parente logico.
            NavUtils.navigateUpTo(this, upIntent);
        }
    }
}
