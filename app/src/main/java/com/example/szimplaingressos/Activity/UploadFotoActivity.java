package com.example.szimplaingressos.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.szimplaingressos.DAO.ConfiguracaoFirebase;
import com.example.szimplaingressos.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.net.URI;

public class UploadFotoActivity extends AppCompatActivity {

    private Button btnUpload;
    private Button btnCancelar;
    private StorageReference storageReference;
    private FirebaseDatabase database;
    private DatabaseReference referenciaFirebase;
    private FirebaseAuth autentificacao;
    private ImageView imageView;
    private String emailUsuarioLogado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_foto);

        storageReference = ConfiguracaoFirebase.getFirebaseStorageReference();

        autentificacao = ConfiguracaoFirebase.getFirebaseAuth();

        emailUsuarioLogado = autentificacao.getCurrentUser().getEmail();

        imageView = (ImageView) findViewById(R.id.imagemCadFotoPerfil);

        carregaImagemPadrao();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem!"), 123);
            }
        });

        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastroFotoPerfil();
            }
        });


    }

    private void cadastroFotoPerfil(){
        StorageReference montaImagemReferencia = storageReference.child("fotoperfilUsuario/" + emailUsuarioLogado + ".jpg");

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();

        Bitmap bitmap = imageView.getDrawingCache();

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArray);
        byte [] data = byteArray.toByteArray();

        UploadTask uploadTask = montaImagemReferencia.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
                carregaImagemPadrao();
                Toast.makeText(UploadFotoActivity.this, "Foto adicionada com sucesso", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        final int heigth = (300);
        final int width = (300);
        if (resultCode == Activity.RESULT_OK) {
        if (requestCode == 123) {
            Uri imagemSelecionada = data.getData();
            Picasso.with(UploadFotoActivity.this).load(imagemSelecionada.toString()).resize(width, heigth).centerCrop().into(imageView);
        }
        }

    }

    private void carregaImagemPadrao() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReferenceFromUrl("gs://szimplaingressos.appspot.com/fotoPerfilUsuario/" + emailUsuarioLogado+".jpg");
        final int heigth = (300);
        final int width = (300);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(UploadFotoActivity.this).load(uri.toString()).resize(width, heigth).centerCrop().into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }



}


