package com.example.maziyyah.light_touch.light_touch.models;

public class CallbackQuery {

    private String callback_query_id;
    private long from_id;
    private long chat_id;

    public CallbackQuery() {
    }
    
    public CallbackQuery(long from_id, long chat_id) {
        this.from_id = from_id;
        this.chat_id = chat_id;
    }
    
    
    public CallbackQuery(String callback_query_id, long from_id, long chat_id) {
        this.callback_query_id = callback_query_id;
        this.from_id = from_id;
        this.chat_id = chat_id;
    }

    public long getFrom_id() {
        return from_id;
    }
    public void setFrom_id(long from_id) {
        this.from_id = from_id;
    }
    public long getChat_id() {
        return chat_id;
    }
    public void setChat_id(long chat_id) {
        this.chat_id = chat_id;
    }

    public String getCallback_query_id() {
        return callback_query_id;
    }

    public void setCallback_query_id(String callback_query_id) {
        this.callback_query_id = callback_query_id;
    }

   

    

    
}
