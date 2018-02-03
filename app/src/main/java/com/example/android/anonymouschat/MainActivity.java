package com.example.android.anonymouschat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView btnPickColor, btnSent;
    EditText editStatus;

    FirebaseDatabase fbRef = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = fbRef.getReference("chats");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycleview);
        btnPickColor = findViewById(R.id.btnPickColor);
        btnSent = findViewById(R.id.btnSend);
        editStatus = findViewById(R.id.editStatus);

        btnSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Data di database ", "data : " + dataSnapshot.getKey());
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    DataSnapshot data = iterator.next();
                    Log.e("Chat time", "Key : " + data.getKey());
                    Log.e("Chat time", "Text : " + data.getChildren());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage() {
        String text = editStatus.getText().toString();
        String chatId = dbRef.push().getKey();
        Map<String, String> chatData = new HashMap<>();
        chatData.put("text", text);
        chatData.put("user", "user-" + chatId);

        Map<String, Object> data = new HashMap<>();
        data.put(chatId, chatData);
        dbRef.updateChildren(data);
        editStatus.setText("");
    }
}
