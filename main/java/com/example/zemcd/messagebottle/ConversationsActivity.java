package com.example.zemcd.messagebottle;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class ConversationsActivity extends AppCompatActivity {

    private static final String EXTRA_CONTACT = "EXTRA_CONTACT";
    private static final String SERVER_IP = "172.90.44.228";
    private static final int PORT = 5555;
    private static final String TAG = "ConversationActivity";

    private RecyclerView mRecyclerView;
    private static List<ConversationHeaderItem> mHeaderItems = new ArrayList<>();
    private static ConversationAdapter mAdapter;
    private static String mUserContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);

        Intent starterIntent = getIntent();
        mUserContact = starterIntent.getStringExtra(EXTRA_CONTACT);
        setTitle(mUserContact + "'s Conversations");

        ConversationHeadersTask headersTask = new ConversationHeadersTask(mUserContact);
        headersTask.setOnCompleteListener(new ConversationHeadersTask.OnCompleteListener() {
            @Override
            public void onComplete() {
                Log.d(TAG, mHeaderItems.toString());
                mAdapter.updateConversations(mHeaderItems
                );
            }
        });
        headersTask.execute();

        mRecyclerView = (RecyclerView) findViewById(R.id.conversation_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new ConversationAdapter(this, mHeaderItems);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onRestart(){
        super.onRestart();
        ConversationHeadersTask headersTask = new ConversationHeadersTask(mUserContact);
        headersTask.setOnCompleteListener(new ConversationHeadersTask.OnCompleteListener() {
            @Override
            public void onComplete() {
                Log.d(TAG, mHeaderItems.toString());
                mAdapter.updateConversations(mHeaderItems
                );
            }
        });
        headersTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_options_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setTitle("Do you want to sign out?")
                .setNegativeButton("No", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getConversationActivityContext(), LoginActivity.class);
                        startActivity(i);
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.newConversation:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final View dialogView = getLayoutInflater().inflate(R.layout.new_conversation_dialog, null, false);
                final EditText et = (EditText) dialogView.findViewById(R.id.contact_input);
                AlertDialog dialog = builder.setTitle("Enter name of contact: ")
                        .setView(dialogView)
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String recipient = et.getText().toString();
                                getAdapter().addConversation(recipient);
                            }
                        })
                        .create();
                dialog.show();
                break;
        }
        return true;
    }

    public static ConversationAdapter getAdapter(){
        return mAdapter;
    }

    public Context getConversationActivityContext(){
        return this;
    }

    public static void setHeaderItems(List<ConversationHeaderItem> items){
        mHeaderItems = items;
    }

    public static String getUser(){
        return mUserContact;
    }

}
