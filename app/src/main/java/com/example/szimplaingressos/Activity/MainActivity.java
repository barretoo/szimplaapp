package com.example.szimplaingressos.Activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.szimplaingressos.Classes.Usuario;
import com.example.szimplaingressos.DAO.ConfiguracaoFirebase;
import com.example.szimplaingressos.Helper.Preferencias;
import com.example.szimplaingressos.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth autenticacao;
    private EditText edtEmailLogin;
    private EditText edtSenhaLogin;
    private Button btnLogin;
    private Usuario usuario;
    private DatabaseReference referenciaDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmailLogin = (EditText) findViewById(R.id.EdtEmail);
        edtSenhaLogin = (EditText) findViewById(R.id.EdtSenha);
        btnLogin = (Button) findViewById(R.id.btnLogin);



        permission();

        if (usuarioLogado()){

            Intent intentMinhaConta = new Intent(MainActivity.this, PrincipalActivity.class);
            abrirNovaActivity(intentMinhaConta);
            finish();

        } else{

          btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edtEmailLogin.getText().toString().equals("") && !edtSenhaLogin.getText().toString().equals("")) {

                    usuario = new Usuario();
                    usuario.setEmail(edtEmailLogin.getText().toString());
                    usuario.setSenha(edtSenhaLogin.getText().toString());

                    validarLogin();

                } else {
                    Toast.makeText(MainActivity.this, "Preencha os campos de E-mail e senha", Toast.LENGTH_SHORT).show();
                }
                }
            });
        }
    }
    private void validarLogin() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail().toString(), usuario.getSenha().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    abrirTelaPrincipal(usuario.getEmail());
                    Preferencias preferencias = new Preferencias(MainActivity.this);
                    preferencias.salvarUsuarioPreferencias(usuario.getEmail(), usuario.getSenha());

                    Toast.makeText(MainActivity.this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                } else  {
                    Toast.makeText(MainActivity.this, "Usuário ou senha inválidos! Tente novamente!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void abrirTelaPrincipal(String emailUsuario){

        Intent intent = new Intent(MainActivity.this, PrincipalActivity.class);
        startActivity(intent);
        finish();

    }

    public Boolean usuarioLogado() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user!=null){
            return true;
            } else {
            return false;
        }
    }



    public void permission(){
        int PERMISSION_ALL = 1;

        String [] PERMISSION = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(MainActivity.this, PERMISSION, PERMISSION_ALL);
    }
    public void abrirNovaActivity (Intent intent){
        startActivity(intent);
    }
}
