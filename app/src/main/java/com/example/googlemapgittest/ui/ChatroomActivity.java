package com.example.googlemapgittest.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.googlemapgittest.R;
import com.example.googlemapgittest.models.Chatroom;

public class ChatroomActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
    }

    @Override
    public void onClick(View v) {

    }
}
