package com.example.zemcd.messagebottle;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class UserConversationActivity extends AppCompatActivity {
    private static final String TAG = "UserConvoActivity";
    private static final String EXTRA_CONTACT = "EXTRA_CONTACT";

    private String mContact;
    private String mUser;
    private RecyclerView mRecyclerView;
    private UserConversationAdapter mAdapter;
    private Button sendButton;
    private static EditText messageField;
    private Conversation mConversation;
    private ArrayList<String> mMessages = new ArrayList<>();
    private boolean isEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_user_conversation);
        mContact = getIntent().getStringExtra(EXTRA_CONTACT);
        setTitle(mContact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.content_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.toolBarText));
        setSupportActionBar(toolbar);

        mUser = ConversationsActivity.getUser();
        messageField = (EditText) findViewById(R.id.msgField);

        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = messageField.getText().toString();
                Log.d(TAG, msg);
                Log.d(TAG, String.valueOf(mAdapter.getItemCount()));
                isEmpty = (mAdapter.getItemCount() > 0) ? true : false;
                SendMessageTask sendTask = new SendMessageTask(msg, mContact, mUser, isEmpty, mAdapter);
                sendTask.execute((Void)null);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.messageRecycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //dummy conversation///////////////
        /*ArrayList<String> msgs = new ArrayList<>();
        msgs.add("this is a text");
        msgs.add("and this is another test");
        msgs.add("and i am once again testing");
        msgs.add("this is more testing and i am writing a longer message to try to see the difference here");
        Conversation dummyConvo = new Conversation("jackson", msgs);*/
        ////end dummy conversation////////

        mConversation = new Conversation(mContact, mMessages);
        mAdapter = new UserConversationAdapter(this, mConversation);
        mRecyclerView.setAdapter(mAdapter);

    }

    public static void clearText(){
        messageField.setText("");
    }

    public static void setError(String error){
        messageField.setError(error);
    }

    public static void clearError(){
        messageField.setError(null);
    }

}
