package com.jgs.socketiochat;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jgs.socketiochat.databinding.ItemChatJoinLeftBinding;
import com.jgs.socketiochat.databinding.ItemChatOtherBinding;
import com.jgs.socketiochat.databinding.ItemChatSelfBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // 채팅 메세지 뷰 확인을 위한 상수 정의
    private static final int ITEM_SELF = 1;
    private static final int ITEM_OTHER = 2;
    private static final int ITEM_JOIN_LEAVE = 3;

    // 현재 시간을 확인하기 위한 객체 초기화
    private SimpleDateFormat format = new SimpleDateFormat("a hh:mm", Locale.KOREAN);
    private Date date = new Date();


    // DiffUtil 콜백 객체를 생성하여 아이템 비교
    private final DiffUtil.ItemCallback<Chat> diffCallback = new DiffUtil.ItemCallback<Chat>() {
        @Override
        public boolean areItemsTheSame(@NonNull Chat oldItem, @NonNull Chat newItem) {
            // 아이템 자체가 같은지 비교
            return oldItem == newItem;
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Chat oldItem, @NonNull Chat newItem) {
            // 아이템의 내용이 같은지 비교
            return oldItem.equals(newItem);
        }
    };

    // AsyncListDiffer를 사용하여 비동기적으로 리스트 업데이트 처리
    private final AsyncListDiffer<Chat> differ = new AsyncListDiffer<>(this, diffCallback);

    // 채팅 목록을 어댑터에 제출하는 메서드
    public void submitChat(List<Chat> chats) {
        differ.submitList(chats);
    }

    // 자신의 채팅 뷰와 상대방의 채팅 뷰를 구분하여 ViewHolder 생성
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SELF) {
            ItemChatSelfBinding binding = ItemChatSelfBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new SelfChatItemViewHolder(binding);
        } else if (viewType == ITEM_OTHER){
            ItemChatOtherBinding binding = ItemChatOtherBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new OtherChatItemViewHolder(binding);
        } else {
            ItemChatJoinLeftBinding binding = ItemChatJoinLeftBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new UserChatJoinLeftViewHolder(binding);
        }
    }

    // 채팅 아이템을 가져와서 ViewHolder에 바인딩
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Chat chat = differ.getCurrentList().get(position);
        if (holder instanceof SelfChatItemViewHolder) {
            // 자신의 채팅일 경우
            ((SelfChatItemViewHolder) holder).bind(chat);
        } else if (holder instanceof OtherChatItemViewHolder){
            // 상대방의 채팅일 경우
            ((OtherChatItemViewHolder) holder).bind(chat);
        } else if (holder instanceof UserChatJoinLeftViewHolder){
            // 유저 입퇴장
            ((UserChatJoinLeftViewHolder) holder).bind(chat);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // 아이템의 뷰 타입을 결정
        Chat chat = differ.getCurrentList().get(position);
        if (chat.getType() == Chat.TYPE_MESSAGE) {
            return chat.isSelf() ? ITEM_SELF : ITEM_OTHER;
        } else {
            return ITEM_JOIN_LEAVE;
        }
    }

    @Override
    public int getItemCount() {
        // 아이템의 총 개수를 반환
        return differ.getCurrentList().size();
    }

    // 상대방의 채팅을 위한 ViewHolder
    public class OtherChatItemViewHolder extends RecyclerView.ViewHolder {
        private final ItemChatOtherBinding binding;

        public OtherChatItemViewHolder(ItemChatOtherBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        // 채팅 데이터를 바인딩하는 메서드
        public void bind(Chat chat) {
            binding.name.setText(chat.getUsername());
            binding.msg.setText(chat.getText());
            binding.tvTime.setText(getNowTime());
        }
    }

    // 자신의 채팅을 위한 ViewHolder
    public class SelfChatItemViewHolder extends RecyclerView.ViewHolder {
        private final ItemChatSelfBinding binding;

        public SelfChatItemViewHolder(ItemChatSelfBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        // 채팅 데이터를 바인딩하는 메서드
        public void bind(Chat chat) {
            binding.name.setText(chat.getUsername());
            binding.msg.setText(chat.getText());
            binding.tvTime.setText(getNowTime());
        }
    }

    // 유저의 입퇴장을 표시하기 위한 ViewHolder
    public class UserChatJoinLeftViewHolder extends RecyclerView.ViewHolder {
        private final ItemChatJoinLeftBinding binding;

        public UserChatJoinLeftViewHolder(ItemChatJoinLeftBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        // 채팅 데이터의 유저명을 바인딩하는 메서드
        public void bind(Chat chat) {
            if (chat.getType() == Chat.TYPE_JOIN) {
                binding.tvUserInfo.setText(chat.getUsername() + "님이 입장하셨습니다.");
            } else if (chat.getType() == Chat.TYPE_LEAVE) {
                binding.tvUserInfo.setText(chat.getUsername() + "님이 퇴장하셨습니다.");
            }
        }
    }

    // 현재 시간을 반환하는 함수
    private String getNowTime() {
        return format.format(date);
    }

}
