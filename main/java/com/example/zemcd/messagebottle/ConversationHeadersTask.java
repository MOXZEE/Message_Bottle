package com.example.zemcd.messagebottle;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ConversationHeadersTask extends AsyncTask<Void, Void, Void>{

    private static final String AUTH_KEY = "9LEF1D97001X!@:";
    private static final String TAG = "HeaderTask";
    private static final String SERVER_IP = "172.90.44.228";
    private static final int PORT = 5555;
    private static final String REQUEST_QUERY_CONTACT = "2";

    private ArrayList<ConversationHeaderItem> mHeaders = new ArrayList<>();
    private String mContact;

    private OnCompleteListener mOnCompleteListener;

    public interface OnCompleteListener{
        void onComplete();
    }

    public void setOnCompleteListener(OnCompleteListener listener){
        mOnCompleteListener = listener;
    }

    public ConversationHeadersTask(String contact){
        mContact = contact;
    }

    @Override
    protected Void doInBackground(Void... params) {
        SocketAddress socketAddress = new InetSocketAddress(SERVER_IP, PORT);
        Socket sock = new Socket();
        try {
            //sock.setSoTimeout(1000);
            Log.d(TAG, "HeadersTask began!");
            sock.connect(socketAddress);
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            Log.d(TAG, "h streams aquired");
            String none = "none";
            out.write(AUTH_KEY + mContact + ":" + none + ":" + REQUEST_QUERY_CONTACT);
            Log.d(TAG, "data sent");
            out.newLine();
            out.flush();

            String recipient;
            String msgHead;
            while ((recipient = in.readLine()) != null){
                Log.d(TAG, recipient);
                msgHead = in.readLine();
                Log.d(TAG, msgHead);
                ConversationHeaderItem item = new ConversationHeaderItem(recipient, msgHead);
                Log.d(TAG, item.toString());
                if (item!=null) mHeaders.add(item);
            }

            out.close();
            in.close();
        } catch (SocketException e) {
            e.printStackTrace();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        return null;
    }

    @Override
    public void onPostExecute(Void result){
        Log.d(TAG, "onPostExecute Called!");
        if (mHeaders != null){
            Log.d(TAG, "headers found!");
            ConversationsActivity.setHeaderItems(mHeaders);
        }else {
            Log.d(TAG, "headers are null");
        }
        mOnCompleteListener.onComplete();
    }
}
