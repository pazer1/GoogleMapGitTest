package com.example.googlemapgittest.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.googlemapgittest.R;
import com.example.googlemapgittest.models.ChatMessage;
import com.example.googlemapgittest.models.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatMessageRecyclerAdapter extends RecyclerView.Adapter<ChatMessageRecyclerAdapter.ViewHolder>{

    private ArrayList<ChatMessage> mMessages = new ArrayList<>();
    private ArrayList<User> mUser = new ArrayList<>();
    private Context context;

    public ChatMessageRecyclerAdapter(ArrayList<ChatMessage> mMessages, ArrayList<User> mUser, Context context) {
        this.mMessages = mMessages;
        this.mUser = mUser;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_chat_message_item,viewGroup,false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageRecyclerAdapter.ViewHolder viewHolder, int i) {

        if(FirebaseAuth.getInstance().getUid().equals(mMessages.get(i).getUser().getUser_id())){
            viewHolder.username.setTextColor(ContextCompat.getColor(context,R.color.green1));
        }else{
            viewHolder.username.setTextColor(ContextCompat.getColor(context,R.color.blue2));
        }
        viewHolder.username.setText(mMessages.get(i).getUser().getUsername());
        viewHolder.messag.setText(mMessages.get(i).getMessage());
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView messag, username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messag = itemView.findViewById(R.id.chat_message_message);
            messag = itemView.findViewById(R.id.chat_message_username);
        }
    }
}
