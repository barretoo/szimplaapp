package com.example.szimplaingressos.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.szimplaingressos.Classes.Cardapio;
import com.example.szimplaingressos.DAO.ConfiguracaoFirebase;
import com.example.szimplaingressos.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class CadastroProdutosActivity extends AppCompatActivity {

    private BootstrapEditText nomeProduto;
    private BootstrapEditText descricao;
    private BootstrapEditText qtdadeProduto;
    private BootstrapEditText preco;
    private BootstrapButton btnCadastrarProduto;
    private BootstrapButton btnCancelar;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Cardapio cardapio;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_produtos);

        nomeProduto = (BootstrapEditText) findViewById(R.id.edtnomeProduto);
        descricao = (BootstrapEditText) findViewById(R.id.edtdescricao);
        qtdadeProduto = (BootstrapEditText) findViewById(R.id.edtqtdadeProduto);
        preco = (BootstrapEditText) findViewById(R.id.edtpreco);
        btnCadastrarProduto = (BootstrapButton) findViewById(R.id.btnCadastrarProduto);
        btnCancelar = (BootstrapButton) findViewById(R.id.btnCancelar);


        btnCadastrarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nomeProduto.getText().toString() != null || descricao.getText().toString() != null || qtdadeProduto.getText().toString() != null || preco.getText().toString() != null){
                    cardapio = new Cardapio();

                    cardapio.setNomeProduto(nomeProduto.getText().toString());
                    cardapio.setDescricao(descricao.getText().toString());
                    cardapio.setPreco(preco.getText().toString());
                    cardapio.setQtdade(qtdadeProduto.getText().toString());
                    cardapio.setUrlImagem("gs://szimplaingressos.appspot.com/SZ eventos -roxa com fundo.png");
                    cardapio.setKeyProduto("4");
                    cadastrarProduto(cardapio);
                    apagaredttext();


                } else  {
                    Toast.makeText(CadastroProdutosActivity.this,"Preencha todos os campos!", Toast.LENGTH_LONG).show();
                }

            }
        });


    }
    private boolean cadastrarProduto(Cardapio cardapio) {

        try {

            reference = ConfiguracaoFirebase.getFirebase().child("cardapio");
            reference.push().setValue(cardapio);
            Toast.makeText(CadastroProdutosActivity.this, "Produto cadastrado com sucesso!", Toast.LENGTH_LONG).show();
            return true;

        } catch (Exception e) {
            Toast.makeText(CadastroProdutosActivity.this, "Erro ao gravar o produto!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        }
    }
    private void apagaredttext() {

        nomeProduto.setText("");
        preco.setText("");
        descricao.setText("");
        qtdadeProduto.setText("");


    }
}
