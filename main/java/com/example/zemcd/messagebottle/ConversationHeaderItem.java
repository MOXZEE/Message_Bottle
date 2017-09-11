package com.example.zemcd.messagebottle;

import java.io.Serializable;

public class ConversationHeaderItem implements Serializable{

    private String contact;
    private String message;

    public ConversationHeaderItem(String contact, String message){
        this.contact = contact;
        this.message = message;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString(){
        return "Contact : " + contact + " - Message Header : " + message;
    }

}
