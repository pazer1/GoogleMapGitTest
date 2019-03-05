package com.example.googlemapgittest.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.googlemapgittest.R;
import com.example.googlemapgittest.UserClient;
import com.example.googlemapgittest.adapters.ChatMessageRecyclerAdapter;
import com.example.googlemapgittest.models.ChatMessage;
import com.example.googlemapgittest.models.Chatroom;
import com.example.googlemapgittest.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class ChatroomActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "ChatroomActivity";

    private  Chatroom mChatroom;
    private EditText mMessage;
    private RecyclerView mChatMessageRecyclerView;
    private ChatMessageRecyclerAdapter chatMessageRecyclerAdapter;
    private FirebaseFirestore mDb;
    private ArrayList<ChatMessage>mMessages = new ArrayList<>();

    private ArrayList<User> mUserList = new ArrayList<>();


    private ListenerRegistration mChatMessageEventListener,mUserListEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        mMessage = findViewById(R.id.input_message);
        mChatMessageRecyclerView = findViewById(R.id.chatmessage_recycler_view);

        findViewById(R.id.checkmark).setOnClickListener(this);

        mDb = FirebaseFirestore.getInstance();

        getIncomingIntent();
        initChatroomRecyclerView();
        getChatroomUsers();
    }


    private void initChatroomRecyclerView(){
        chatMessageRecyclerAdapter = new ChatMessageRecyclerAdapter(mMessages,new ArrayList<User>(),this);
        mChatMessageRecyclerView.setAdapter(chatMessageRecyclerAdapter);
        mChatMessageRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mChatMessageRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener(){

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(bottom<oldBottom){
                    mChatMessageRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(mMessages.size()>0){
                                mChatMessageRecyclerView.smoothScrollToPosition(
                                        mChatMessageRecyclerView.getAdapter().getItemCount()-1);
                            }
                        }
                    },100);
                }
            }
        });
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra(getString(R.string.intent_chatroom))){
            mChatroom = getIntent().getParcelableExtra(getString(R.string.intent_chatroom));
            setChatroomName();
            joinChatroom();
        }
    }

    private void getChatroomUsers(){

        CollectionReference userRef = mDb.collection("Chatrooms").
                document(mChatroom.getChatroom_id()).collection("User List");

        mUserListEventListener = userRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.e(TAG,"onEvent: Listen failed.",e);
                    return;
                }
                if(queryDocumentSnapshots != null){
                    mUserList.clear();
                    mUserList = new ArrayList<>();

                    for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                        User user = doc.toObject(User.class);
                        mUserList.add(user);
                    }
                }
            }
        });


    }

    private void joinChatroom(){

        DocumentReference joinChatroomRef = mDb.collection(getString(R.string.collection_chatrooms))
                .document(mChatroom.getChatroom_id())
                .collection(getString(R.string.collection_chatroom_user_list))
                .document(FirebaseAuth.getInstance().getUid());

        User user = ((UserClient)(getApplicationContext())).getUser();
        joinChatroomRef.set(user);
    }

    private void setChatroomName(){
        getSupportActionBar().setTitle(mChatroom.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void insertNewMessage(){
        //TODO 여기부터
        String message = mMessage.getText().toString();

        if(!message.equals("")){
            message = message.replaceAll(System.getProperty("line.separator"),"");

            DocumentReference newMessageDoc = mDb.collection("Chatrooms").document(mChatroom.getChatroom_id())
                    .collection("Chat Messages").document();

            
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.checkmark:{
                insertNewMessage();
            }
        }
    }
}
