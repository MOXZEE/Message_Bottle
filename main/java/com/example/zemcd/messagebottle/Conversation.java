package com.example.zemcd.messagebottle;

import java.util.ArrayList;

public class Conversation {
    private String contact;
    private ArrayList<String> messages;

    public Conversation(String contact, ArrayList<String> messages){
        this.contact = contact;
        this.messages = messages;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }
}
