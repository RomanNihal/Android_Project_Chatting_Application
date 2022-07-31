package com.example.ourchat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Connector extends RecyclerView.Adapter<Connector.ViewHolder> {

    List<ModelOfMessage> messages;
    Context context;

    public Connector(List<ModelOfMessage> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public Connector.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.model_of_recycler_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Connector.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
    holder.name.setText(messages.get(position).getNameOfUser());
    holder.message.setText(messages.get(position).getMessageOfUser());
     Glide.with(context).load(messages.get(position).getPictureOfUser()).into(holder.picture);
     holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
         @Override
         public boolean onLongClick(View v) {
             String mail=FirebaseAuth.getInstance().getCurrentUser().getEmail();
             if(messages.get(position).getMailOfUser().equals(mail)){
                 CharSequence[] items = {"Delete"};
                 AlertDialog.Builder dialog = new AlertDialog.Builder(context);

                 dialog.setItems(items, new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         if(i == 0){
                             String key=messages.get(position).getIdOfKey();
                             DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
                             databaseReference.child("chats").child(key).removeValue();
                         }
                     }
                 });
                 dialog.show();
             }
             return false;
         }
     });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picture;
        TextView name, message;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.modelImage);
            name = itemView.findViewById(R.id.modelUserName);
            message = itemView.findViewById(R.id.modelMessage);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
