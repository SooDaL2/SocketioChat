package com.jgs.socketiochat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jgs.socketiochat.databinding.ActivityChatBinding;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;

    private static String WithCorn_Chat_URL = BuildConfig.WITHCORN_CHAT_URL;

    private Socket mSocket;
    private String username;
    private String roomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // binding
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ChatServerInit();
    }

    private void ChatServerInit() {
        // Socket.io를 이용한 채팅서버 연결
        try {
            mSocket = IO.socket(WithCorn_Chat_URL);
            Toast.makeText(ChatActivity.this, "Connected : " + mSocket.id(), Toast.LENGTH_LONG).show();
        } catch (URISyntaxException e) {
            Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // 입력받은 Username과 RoomNumber로 채팅방 생성
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        roomNumber = intent.getStringExtra("roomNumber");

        binding.tvUsername.setText(username);
        binding.tvRoomNumber.setText(roomNumber);

        mSocket.connect();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // ChatActivity 종료 시 채팅서버와 연결 종료
        mSocket.disconnect();
    }

}