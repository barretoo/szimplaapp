package com.example.szimplaingressos.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.szimplaingressos.Classes.Usuario;
import com.example.szimplaingressos.DAO.ConfiguracaoFirebase;
import com.example.szimplaingressos.Helper.Preferencias;
import com.example.szimplaingressos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha1;
    private EditText senha2;
    private EditText nome;
    private RadioButton rbAdm;
    private RadioButton rbPDV;
    private RadioButton rbVal;
    private Button btnCadastrar;
    private Button btnCancaelar;
    private FirebaseAuth autenticacao;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrousuario);

        email = (EditText) findViewById(R.id.EdtCadEmail);
        senha1 = (EditText) findViewById(R.id.EdtCadSenha1);
        senha2 = (EditText) findViewById(R.id.EdtCadSenha2);
        nome = (EditText) findViewById(R.id.EdtCadNome);
        rbAdm = (RadioButton) findViewById(R.id.rbAdm);
        rbPDV = (RadioButton) findViewById(R.id.rbPDV);
        rbVal = (RadioButton) findViewById(R.id.rbVal);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        btnCancaelar = (Button) findViewById(R.id.btnCancelar);


        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (senha1.getText().toString().equals(senha2.getText().toString())){
                    usuario = new Usuario();

                    usuario.setEmail(email.getText().toString());
                    usuario.setSenha(senha1.getText().toString());
                    usuario.setNome(nome.getText().toString());

                    if (rbAdm.isChecked()){
                        usuario.setTipoUsuario("Administrador");
                    } else if (rbPDV.isChecked()){
                        usuario.setTipoUsuario("PDV");
                    } else if (rbVal.isChecked()){
                        usuario.setTipoUsuario("Validador");
                    }

                    cadastrarUsuario();
                } else  {
                    Toast.makeText(CadastroUsuarioActivity.this,"As senhas não se correspondem!", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void cadastrarUsuario() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    insereUsuario(usuario);

                    finish();

                    //deslogar ao adicionar o usuário
                    autenticacao.signOut();

                    //para abrir a nossa tela principal após a re-autenticação
                    abreTelaPrincipal();


                } else {

                    String erroExcecao = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = "Digite uma senha mais forte, contendo no mínimo 8 caracteres e que contenha letras e números!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "O e-mail digitado é invalido, digite um novo e-mail";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Esse e-mail já está cadastro!";
                    } catch (Exception e) {
                        erroExcecao = "Erro ao efetuar o cadastro!";
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroUsuarioActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private boolean insereUsuario(Usuario usuario) {

        try {

            reference = ConfiguracaoFirebase.getFirebase().child("usuarios");
            reference.push().setValue(usuario);
            Toast.makeText(CadastroUsuarioActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();
            return true;

        } catch (Exception e) {
            Toast.makeText(CadastroUsuarioActivity.this, "Erro ao gravar o usuário!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        }
    }

    private void abreTelaPrincipal() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();

        Preferencias preferencias = new Preferencias(CadastroUsuarioActivity.this);

        autenticacao.signInWithEmailAndPassword(preferencias.getEmailUsuarioLogado(), preferencias.getSenhaUsuarioLogado()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Intent intent = new Intent(CadastroUsuarioActivity.this, PrincipalActivity.class);
                    startActivity(intent);
                    finish();
                } else {

                    Toast.makeText(CadastroUsuarioActivity.this, "Falha!", Toast.LENGTH_LONG).show();
                    autenticacao.signOut();
                    Intent intent = new Intent(CadastroUsuarioActivity.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                }

            }

        });

    }
}
