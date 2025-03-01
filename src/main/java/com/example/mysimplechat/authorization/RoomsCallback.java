package com.example.mysimplechat.authorization;

import com.example.mysimplechat.chat.chatroom.ChatRoom;

import java.io.IOException;
import java.util.List;

public interface RoomsCallback {
    void onSuccess(List<ChatRoom> chatRooms);
    void onError(IOException e);
}
