package com.jgs.socketiochat;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jgs.socketiochat.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        JoinInit();
    }
    
    private void JoinInit() {
        binding.btnJoin.setOnClickListener(v -> {
            Intent chatIntent = new Intent(MainActivity.this, ChatActivity.class);
            chatIntent.putExtra("username", binding.etUsername.getText().toString());
            chatIntent.putExtra("roomNumber", binding.etRoomNumber.getText().toString());
            startActivity(chatIntent);
        });
    }
    
}