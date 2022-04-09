package com.example.nftproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<ModelClass> userList ;

    public Adapter(List<ModelClass>userList){this.userList=userList;}


    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collectibles,parent,false);
        return new ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        String ID = userList.get(position).getID();
        String URI = userList.get(position).getURI();
        String owner = userList.get(position).getOwner();
        holder.setData(ID, URI, owner) ;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView IDText ;
        private TextView URIText ;
        private TextView ownerText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            IDText = itemView.findViewById(R.id.IDText) ;
            URIText = itemView.findViewById(R.id.URIText) ;
            ownerText = itemView.findViewById(R.id.ownerText);
        }

        public void setData(String ID, String URI, String owner) {
            IDText.setText(ID);
            URIText.setText(URI);
            ownerText.setText(owner);
        }
    }
}
