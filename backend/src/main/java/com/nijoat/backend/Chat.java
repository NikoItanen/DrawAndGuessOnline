package com.nijoat.backend;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Chat {
    private static List<Message> messages = new ArrayList<>();

    @PostMapping("/sendmessage")
    public String sendMessage(@RequestBody Message message) {
        messages.add(message);
        return "Message sent successfully";
    }

    @GetMapping("/messages")
    public List<Message> getMessages() {
        return messages;
    }

    public static class Message {
        private String sender;
        private String content;

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}