package com.example.szimplaingressos.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.szimplaingressos.Classes.Usuario;
import com.example.szimplaingressos.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PrincipalActivity extends AppCompatActivity {


    private FirebaseAuth autenticacao;
    private DatabaseReference referenceFirebase;
    private TextView tipoUsuario;
    private Usuario usuario;
    private String tipoUsuarioEmail;
    private LinearLayout linearLayoutAddProdutos;
    private LinearLayout linearLayoutTotalVendido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);


        autenticacao = FirebaseAuth.getInstance();
        referenceFirebase = FirebaseDatabase.getInstance().getReference();
        linearLayoutTotalVendido = (LinearLayout) findViewById(R.id.linearLayoutProdutoCardapio);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.clear();

        getMenuInflater().inflate(R.menu.menu_admin, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_add_usuario) {
            abrirtelaCadastroUsuario();
        }   else if (id == R.id.action_cardapio){
                abrirtelaCardapio();
        } else if (id == R.id.action_cadProduto){
            abrirtelaCadastroProdutos();
        } else if (id == R.id.action_sair_admin){
                deslogarUsuario();
        }
        return super.onOptionsItemSelected(item);
    }

    private void abrirtelaCadastroProdutos(){
        Intent intent = new Intent(PrincipalActivity.this, CadastroProdutosActivity.class);
        startActivity(intent);
    }


    private void abrirtelaCadastroUsuario(){
        Intent intent = new Intent(PrincipalActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }

    private void abrirtelaCardapio(){
        Intent intent = new Intent(PrincipalActivity.this, CardapioActivity.class);
        startActivity(intent);
    }
    private void deslogarUsuario(){

        autenticacao.signOut();
        Intent intent = new Intent(PrincipalActivity.this, MainActivity.class);
        finish();
    }
    private void uploadFotoPerfil(){
        Intent intent = new Intent(PrincipalActivity.this, UploadFotoActivity.class);

        startActivity(intent);
    }

}



