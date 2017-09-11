package com.example.zemcd.messagebottle;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class LoginTask extends AsyncTask<Void, Void, Void>{

    private static final String TAG = "LoginTask";
    private static final String REQUEST_LOGIN = "1";
    private static final String AUTH_KEY = "9LEF1D97001X!@:";
    private static final int PORT = 5555;
    private static final String EXTRA_CONTACT = "EXTRA_CONTACT";

    private String mContact;
    private String mPassword;
    private Context mContext;

    private Socket mSocket;
    private boolean valid = false;
    private boolean error = true;

    private NewAccountTask.OnFailureListener mOnFailureListener;

    public void setOnFailureListener(NewAccountTask.OnFailureListener listener){
        mOnFailureListener = listener;
    }

    public LoginTask(String contact, String password, Context context){
        mContact = contact + ":";
        mPassword = password + ":";
        mContext = context;
    }

    public String getContactExtra(){
        return mContact.replace(":", "");
    }

    @Override
    protected Void doInBackground(Void... params) {
        try{
            mSocket = new Socket();
            //mSocket.setSoTimeout(300);
            Log.d(TAG, "socket attempting to connect");
            SocketAddress socketAddress = new InetSocketAddress("172.90.44.228", PORT);
            mSocket.connect(socketAddress);

            Log.d(TAG, "socket connected");

            BufferedReader in = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));

            Log.d(TAG, "streams aquired");

            out.write(AUTH_KEY + mContact + mPassword + REQUEST_LOGIN);
            out.newLine();
            out.flush();

            Log.d(TAG, "data sent");

            String response;
            response = in.readLine();

            Log.d(TAG, "data recieved");

            switch (response){
                case "failed":
                    Log.d(TAG, "validation failed");
                    break;
                case "false":
                    error = false;
                    valid = false;
                    Log.d(TAG, "valid = false");
                    break;
                case "true":
                    error = false;
                    valid = true;
                    Log.d(TAG, "valid = true");
                    break;
                default:
                    Log.d(TAG, "invalid response from server");
                    break;
            }

        }catch (IOException ioe){
            Log.e(TAG, "error connecting");
            LoginActivity.resetCompletionStatus();
            mOnFailureListener.onFailure();
        }finally {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public void onPostExecute(Void result){
        if (error){
            Log.d(TAG, "validation failed");
            return;
        }
        if (valid){
            Intent i = new Intent(mContext, ConversationsActivity.class);
            i.putExtra(EXTRA_CONTACT, getContactExtra());
            mContext.startActivity(i);
        }else {
            new AlertDialog.Builder(mContext)
                    .setTitle("Incorrect username or password")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(mContext, LoginActivity.class);
                            mContext.startActivity(i);
                        }
                    })
                    .create().show();
        }

    }
}
