package com.example.zemcd.messagebottle;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.StringTokenizer;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationHeaderHolder>{
    private static final String EXTRA_CONTACT = "EXTRA_CONTACT";

    private Context mContext;
    private List<ConversationHeaderItem> mHeaderItems;

    public ConversationAdapter(Context context, List<ConversationHeaderItem> headerItems){
        mContext = context;
        mHeaderItems = headerItems;
    }

    public void updateConversations(List<ConversationHeaderItem> headers){
        mHeaderItems.clear();
        mHeaderItems.addAll(headers);
        notifyDataSetChanged();
    }

    public void addConversation(String recipient){
        ConversationHeaderItem item = new ConversationHeaderItem(recipient, null);
        mHeaderItems.add(item);
        notifyDataSetChanged();
    }

    @Override
    public ConversationHeaderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.conversation_item, parent, false);

        return new ConversationHeaderHolder(v);
    }

    @Override
    public void onBindViewHolder(ConversationHeaderHolder holder, int position) {
        holder.bindItems(mHeaderItems.get(position));
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return mHeaderItems.size();
    }

    public class ConversationHeaderHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private static final String TAG = "HeaderHolder";

        private TextView contactView;
        private TextView msgView;
        private int position;

        public ConversationHeaderHolder(View itemView){
            super(itemView);
            contactView = (TextView) itemView.findViewById(R.id.conv_cont_view);
            msgView = (TextView) itemView.findViewById(R.id.conv_msg_view);
            itemView.setOnClickListener(this);
        }

        public void setPosition(int position){
            this.position = position;
        }

        public void bindItems(ConversationHeaderItem item){
            contactView.setText(item.getContact());
            msgView.setText(item.getMessage());
        }

        @Override
        public void onClick(View v) {
            Log.i(TAG, String.valueOf(position));
            Intent i = new Intent(mContext, UserConversationActivity.class);
            i.putExtra(EXTRA_CONTACT, contactView.getText());
            mContext.startActivity(i);
        }
    }//end ViewHolder subclass

}//end Adapter subclass
