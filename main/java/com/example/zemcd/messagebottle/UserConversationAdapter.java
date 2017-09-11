package com.example.zemcd.messagebottle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class UserConversationAdapter extends RecyclerView.Adapter<UserConversationAdapter.MessageHolder>{

    private Context mContext;
    private String mContact;
    private ArrayList<String> mMessages;

    public UserConversationAdapter(Context context, Conversation conversation){
        mContext = context;
        mContact = conversation.getContact();
        mMessages = conversation.getMessages();
    }

    public void updateItems(String message){
        mMessages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.message_item, parent, false);
        return new MessageHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageHolder holder, int position) {
        holder.bindItems(mMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class MessageHolder extends RecyclerView.ViewHolder{

        private TextView msgView;

        public MessageHolder(View itemView){
            super(itemView);
            msgView = (TextView) itemView.findViewById(R.id.msgView);
        }

        public void bindItems(String msg){
            msgView.setText(msg);
        }
    }//end viewholder subclass
}//end adapter subclass
