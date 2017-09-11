package com.example.zemcd.messagebottle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class NewAccountTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "NewAccountTask";
    private static final String AUTH_KEY = "9LEF1D97001X!@:";
    private static final String REQUEST_NEW_ACCOUNT = "0";
    private static final int PORT = 5555;

    private String mContactId;
    private String mPassword;
    private Context mContext;
    private ProgressBar mProgressBar;


    private Socket mSocket;
    private String response;

    private boolean failed = false;

    public interface TaskCompleteListener{
        void onTaskComplete();
    }

    public interface OnFailureListener{
        void onFailure();
    }

    private TaskCompleteListener mTaskCompleteListener;
    private OnFailureListener mOnFailureListener;

    public void setOnTaskCompleteListener(TaskCompleteListener listener){mTaskCompleteListener = listener; }
    public void setOnFailureListener(OnFailureListener listener){
        mOnFailureListener = listener;
    }

    public NewAccountTask(String contactId, String password, Context context, ProgressBar progressBar){
        mContactId = contactId + ":";
        mPassword = password + ":";
        mContext = context;
        mProgressBar = progressBar;
    }

    @Override
    public void onPreExecute(){
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... params) {
        try{
            mSocket = new Socket();
            //mSocket.setSoTimeout(300);
            SocketAddress socketAddress = new InetSocketAddress("172.90.44.228", PORT);//"172.90.44.228" "192.168.0.111"
            mSocket.connect(socketAddress);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            Log.d(TAG, "streams aquired");

            out.write(AUTH_KEY + mContactId + mPassword + REQUEST_NEW_ACCOUNT);
            Log.d(TAG, "data sent");
            out.newLine();
            out.flush();

            Log.d(TAG, "read began");
            response = in.readLine();
            out.close();
            in.close();
            Log.d(TAG, "streams closed");

            Log.i(TAG, "connection success!");
        }catch (IOException ioe){
            failed = true;
            mOnFailureListener.onFailure();
            Log.e(TAG, "error connecting", ioe);
        }

        return null;
    }

    @Override
    public void onPostExecute(Void result){
        Log.d(TAG, "onPostExecute called!");
        LoginActivity.resetCompletionStatus();
        mProgressBar.setVisibility(ProgressBar.GONE);
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        if (response!=null){
            mTaskCompleteListener.onTaskComplete();
            AlertDialog msgDialog = dialog.setTitle("Success")
                    .setNegativeButton("OK", null)
                    .create();
            msgDialog.show();
        }else {
            mTaskCompleteListener.onTaskComplete();
            AlertDialog msgDialog = dialog.setTitle("Account creation failed (possible duplicate)")
                    .setNegativeButton("OK", null)
                    .create();
            msgDialog.show();
            //Intent i = new Intent(mContext, LoginActivity.class);
            //mContext.startActivity(i);
        }

    }

    /*public boolean isFailed(){
        return failed;
    }*/

    /*public void clearFailure(){
        failed = false;
    }*/
}
