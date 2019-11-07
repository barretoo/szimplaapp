package com.example.szimplaingressos.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.szimplaingressos.Adapter.CardapioAdapter;
import com.example.szimplaingressos.Classes.Cardapio;
import com.example.szimplaingressos.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CardapioActivity extends AppCompatActivity {

    private RecyclerView mRecyclerViewCardapios;
    private CardapioAdapter adapter;
    private List<Cardapio> cardapios;
    private DatabaseReference referenciaFirebase;
    private Cardapio todosCardapios;
    private LinearLayoutManager mLayoutManagerTodosProdutos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);

        mRecyclerViewCardapios = (RecyclerView) findViewById(R.id.recycleViewTodosProdutos);


        carregarTodosProdutos();

    }


    private void carregarTodosProdutos(){
        mRecyclerViewCardapios.setHasFixedSize(true);

        mLayoutManagerTodosProdutos = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerViewCardapios.setLayoutManager(mLayoutManagerTodosProdutos);

        cardapios = new ArrayList<>();

        referenciaFirebase = FirebaseDatabase.getInstance().getReference();

        referenciaFirebase.child("cardapio").orderByChild("nomeProduto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){

                    todosCardapios = postSnapshot.getValue(Cardapio.class);

                    cardapios.add(todosCardapios);



                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter = new CardapioAdapter(cardapios, this);

        mRecyclerViewCardapios.setAdapter(adapter);


    }

}
