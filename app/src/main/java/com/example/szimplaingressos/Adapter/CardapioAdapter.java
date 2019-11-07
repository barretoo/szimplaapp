package com.example.szimplaingressos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.szimplaingressos.Classes.Cardapio;
import com.example.szimplaingressos.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CardapioAdapter extends RecyclerView.Adapter<CardapioAdapter.ViewHolder> {

    private List<Cardapio> mCardapioList;
    private Context context;
    private List<Cardapio> cardapios;
    private Cardapio todosProdutos;
    private DatabaseReference referenciaFirabase;
    ElegantNumberButton numberButton;
    Integer num;



    public CardapioAdapter (List<Cardapio> l, Context c) {
        context = c;
        mCardapioList = l;

    }

    @NonNull
    @Override
    public CardapioAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lista_todos_produtos, viewGroup, false);

        return new CardapioAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CardapioAdapter.ViewHolder holder, int position) {

        final Cardapio item = mCardapioList.get(position);

        cardapios = new ArrayList<>();

        referenciaFirabase = FirebaseDatabase.getInstance().getReference();

        referenciaFirabase.child("cardapio").orderByChild("nomeProduto").equalTo(item.getNomeProduto()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cardapios.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    todosProdutos = postSnapshot.getValue(Cardapio.class);

                    cardapios.add(todosProdutos);



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.txtNomeProdutoCardapio.setText(item.getNomeProduto());
        holder.txtDescricaoProdutoCardapio.setText(item.getDescricao());
        holder.txtPrecoProdutoCardapio.setText("R$ " + item.getPreco());
        holder.txtQtdadeProdutoCardapio.setText(item.getQtdade() + " unidades");


                holder.linearLayoutProdutoCardapio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });



    }

    @Override
    public int getItemCount() {
        return mCardapioList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView txtNomeProdutoCardapio;
        protected TextView txtDescricaoProdutoCardapio;
        protected TextView txtPrecoProdutoCardapio;
        protected TextView txtQtdadeProdutoCardapio;

        protected LinearLayout linearLayoutProdutoCardapio;


        public ViewHolder (View itemView) {
            super(itemView);

            txtNomeProdutoCardapio = (TextView)itemView.findViewById(R.id.txtNomeProdutoCardapio);
            txtDescricaoProdutoCardapio = (TextView)itemView.findViewById(R.id.txtDescricaoProdutoCardapio);
            txtPrecoProdutoCardapio = (TextView)itemView.findViewById(R.id.txtPrecoProdutoCardapio);
            txtQtdadeProdutoCardapio = (TextView)itemView.findViewById(R.id.txtQtdadeProdutoCardapio);

            linearLayoutProdutoCardapio = (LinearLayout) itemView.findViewById(R.id.linearLayoutProdutoCardapio);



        }

    }
}
