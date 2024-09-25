package com.jgs.socketiochat;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketHandler {

    private Socket socket;

    private static final String WithCorn_Chat_URL = BuildConfig.WITHCORN_CHAT_URL;

    private static final String NEW_MESSAGE = "new_message";
    private static final String BROADCAST = "broadcast";

    private static final String USER_JOIN_ROOM = "clientToServer_username";
    private static final String JOIN_MESSAGE = "serverToClient_username";
    private static final String USER_LEAVE_ROOM = "leaveRoom";
    private static final String LEAVE_MESSAGE = "leaveMsg";

    private MutableLiveData<Chat> _onNewChat = new MutableLiveData<>();
    private LiveData<Chat> onNewChat = _onNewChat;

    // 외부에서 채팅 메세지를 확인하기 위한 함수
    public LiveData<Chat> getOnNewChat() {
        return onNewChat;
    }

    // IO.socket을 사용한 소켓 연결을 초기화한 생성자
    public SocketHandler() {
        try {
            socket = IO.socket(WithCorn_Chat_URL);
            socket.connect();
            registerOnNewChat();
            userJoinChat();
//            userLeaveChat();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    // 소켓 연결을 해제하는 메서드
    public void disconnectSocket() {
        if (socket != null) {
            socket.disconnect();
            socket.off();
        }
    }

    // 새로운 채팅 메세지를 수신하여 화면에 보여주기 위한 메서드
    private void registerOnNewChat() {
        socket.on(BROADCAST, args -> {
            if (args != null && args.length > 0) {
                Object data = args[0];
                Log.d("ChatDataDebug", data.toString());
                if (!data.toString().isEmpty()) {
                    Chat chat = new Gson().fromJson(data.toString(), Chat.class);
                    _onNewChat.postValue(chat);
                }
            }
        });
    }

    // 새로운 채팅 메세지를 발신하는 메서드
    public void emitChat(Chat chat) {
        String jsonStr = new Gson().toJson(chat, Chat.class);
        socket.emit(NEW_MESSAGE, jsonStr);
    }

    // 채팅방에 유저가 접속했을 때 유저이름을 서버에 보내는 메서드
    public void emitJoinUser(Chat chat) {
        String jsonStr = new Gson().toJson(chat, Chat.class);
        socket.emit(USER_JOIN_ROOM, jsonStr);
    }

    // 채팅방에 유저가 들어왔을 때 알림을 처리하는 메서드
    public void userJoinChat() {
        socket.on(JOIN_MESSAGE, args -> {
            if (args != null && args.length > 0) {
                Object data = args[0];
                if (!data.toString().isEmpty()) {
                    Chat joinMsg = new Gson().fromJson(data.toString(), Chat.class);
                    _onNewChat.postValue(joinMsg);
                    Log.i("JoinUser", data.toString());
                }
            }
        });
    }

    // 채팅방에서 유저가 퇴장했을 때 유저이름을 서버에 보내는 메서드
//    public void emitLeaveUser(Chat chat) {
//        String jsonStr = new Gson().toJson(chat, Chat.class);
//        socket.emit(USER_LEAVE_ROOM, jsonStr);
//    }

    // 채팅방에서 유저가 퇴장했을 때 알림을 처리하는 메서드
//    public void userLeaveChat() {
//        socket.on(LEAVE_MESSAGE, args -> {
//            if (args != null && args.length > 0) {
//                Object data = args[0];
//                if (!data.toString().isEmpty()) {
//                    Chat leaveMsg = new Gson().fromJson(data.toString(), Chat.class);
//                    _onNewChat.postValue(leaveMsg);
//                    Log.i("LeaveUser", data.toString());
//                }
//            }
//        });
//    }

}
