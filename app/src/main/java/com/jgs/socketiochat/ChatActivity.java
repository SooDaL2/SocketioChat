package com.jgs.socketiochat;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jgs.socketiochat.databinding.ActivityChatBinding;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;

    private SocketHandler socketHandler;
    private ChatAdapter chatAdapter;

    private List<Chat> chatList = new ArrayList<>();
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // binding
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 채팅방에 들어올 때 입력한 유저이름을 확인
        userName = getIntent().getStringExtra("username");
        if (userName == null || userName.isEmpty()) {
            finish();
            return;
        } else {
            // socketHandler와 chatAdapter 초기화
            socketHandler = new SocketHandler();
            chatAdapter = new ChatAdapter();

            // LinearLayoutManager와 chatAdapter를 사용하여 RecyclerView 설정
            binding.rvChat.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
            binding.rvChat.setAdapter(chatAdapter);

            // 채팅 작성 후 보내기 버튼 클릭
            binding.btnSend.setOnClickListener(msgSendClickListener);

            // 소켓에서 새로운 채팅 메세지를 수신하면 뷰에 추가
            socketHandler.getOnNewChat().observe(this, chatObserver);
        }
    }

    @Override
    protected void onDestroy() {
        // ChatActivity 종료 시 채팅서버와 연결 종료
        socketHandler.disconnectSocket();
        super.onDestroy();
    }


    // 작성한 채팅 서버로 보내기
    View.OnClickListener msgSendClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String message = binding.etMsg.toString();
            if (!message.isEmpty()) {
                Chat chat = new Chat(userName, message);
                socketHandler.emitChat(chat);
                binding.etMsg.setText("");
            }
        }
    };

    // Socket에서 새로 들어오는 채팅이 있는지 관찰
    Observer<Chat> chatObserver = new Observer<Chat>() {
        @Override
        public void onChanged(Chat chat) {
            Chat newChat = new Chat(chat.getUsername(), chat.getText(), chat.getUsername().equals(userName));
            chatList.add(newChat);  // 채팅리스트에 새로운 채팅 추가
            chatAdapter.submitChat(chatList);   // 새로운 채팅리스트를 어댑터에 추가
            binding.rvChat.scrollToPosition(chatList.size() - 1);   // 최신 메세지로 스크롤
        }
    };

}
