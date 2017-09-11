package com.example.zemcd.messagebottle

import android.os.AsyncTask
import android.util.Log

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress

class SendMessageTask
(private val mMessage: String,
 private val mContact: String,
 private val mUser: String,
 private val mIsEmpty: Boolean,
 private val mAdapter: UserConversationAdapter) : AsyncTask<Void, Void, Void>() {

    private var mSocket: Socket? = null
    private var mSuccess: Boolean = false

    override fun onPreExecute(){
        UserConversationActivity.clearError()
    }

    override fun doInBackground(vararg params: Void): Void? {
        mSocket = Socket()
        Log.d(TAG, "SendTask Started!")
        val socketAddress = InetSocketAddress(SERVER_IP, PORT)

        mSuccess = try {
            mSocket!!.connect(socketAddress)
            val reader = BufferedReader(InputStreamReader(mSocket!!.getInputStream()))
            val out = BufferedWriter(OutputStreamWriter(mSocket!!.getOutputStream()))
            Log.d(TAG, "streams aquired!")

            out.write("${AUTH_KEY}${mContact}:${mMessage}:${REQUEST_SEND}:${mUser}:${mIsEmpty.toString()}")
            out.newLine()
            out.flush()

            Log.d(TAG, "data sent!")

            if (reader.readLine().equals("false")) throw IOException()

            out.close()
            reader.close()

            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }

        return null
    }

    override fun onPostExecute(result: Void?){
        if (mSuccess){
            mAdapter.updateItems(mMessage)
            UserConversationActivity.clearText()
        }else UserConversationActivity.setError("Failed to send!")
    }

    companion object {
        private val TAG = "SendTask"
        private val AUTH_KEY = "9LEF1D97001X!@:"
        private val SERVER_IP = "172.90.44.228"
        private val PORT = 5555
        private val REQUEST_SEND = "3";
    }
}
