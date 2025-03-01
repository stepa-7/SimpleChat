package com.example.mysimplechat.chat.chatmessage;

import java.io.IOException;
import java.util.List;

public interface MessagesCallback {
    void onSuccess(List<ChatMessage> messages);
    void onError(IOException e);
}
