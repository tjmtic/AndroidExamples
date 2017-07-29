package com.abyxcz.application.androidexamples.socketChat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.abyxcz.application.androidexamples.ExampleApplication;
import com.abyxcz.application.androidexamples.LoginActivity;
import com.abyxcz.application.androidexamples.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * A chat fragment containing messages view and input form.
 */
public class ChatFragment extends Fragment {

    private final String TAG = "=~_~=tijAmAtic=~_~=";

    private static final int REQUEST_LOGIN = 0;

    private static final int TYPING_TIMER_LENGTH = 600;

    private RecyclerView mMessagesView;
    private EditText mInputMessageView;
    private List<Message> mMessages = new ArrayList<Message>();
    private RecyclerView.Adapter mAdapter;
    private boolean mTyping = false;
    private Handler mTypingHandler = new Handler();
    private String mUsername="Guest";
    private Socket mSocket;
    private Socket mNSSocket;

    private Boolean isConnected = true;

    public ChatFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAdapter = new MessageAdapter(activity, mMessages);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setHasOptionsMenu(true);

        ExampleApplication app = (ExampleApplication) getActivity().getApplication();
        mUsername = app.getUser().getUsername();
        //mNSSocket = app.getNSSocket();
        mSocket = app.getSocket();
        //mSocket = app.getSocketForNamespace(5);

        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("new message", onNewMessage);
        mSocket.on("user joined", onUserJoined);
        mSocket.on("user left", onUserLeft);
        mSocket.on("typing", onTyping);
        mSocket.on("stop typing", onStopTyping);
        mSocket.on("msgUpdate", onNewLog);
        mSocket.on("new room made", onNewRoomMade);
        mSocket.on("new room", onNewRoom);
        mSocket.connect();

        /*mNSSocket.on(Socket.EVENT_CONNECT,onConnect);
        mNSSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        mNSSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mNSSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mNSSocket.on("new message", onNewMessage);
        mNSSocket.on("user joined", onUserJoined);
        mNSSocket.on("user left", onUserLeft);
        mNSSocket.on("typing", onTyping);
        mNSSocket.on("stop typing", onStopTyping);
        mNSSocket.on("msgUpdate", onNewLog);
        mNSSocket.connect();*/

        //startSignIn();
    }

    public void logThing(String thing){

    }

    public void setNSSocket(int ns){


        mSocket.emit("new room", ns);


        Log.d(TAG, "resetting socket to NS " + ns);


        try{
            //disconnectSocket(mNSSocket);
            //disconnectSocket(mSocket);
            mSocket.disconnect();
        }catch(NullPointerException e){
            Log.d(TAG, "mNSSocket was null");
        }

        ExampleApplication app = (ExampleApplication) getActivity().getApplication();
        //mNSSocket = app.getSocketForNamespace(ns);
        mSocket = app.getSocket();

       /* mNSSocket.on(Socket.EVENT_CONNECT,onConnect);
        mNSSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        mNSSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mNSSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mNSSocket.on("new message", onNewMessage);
        mNSSocket.on("user joined", onUserJoined);
        mNSSocket.on("user left", onUserLeft);
        mNSSocket.on("typing", onTyping);
        mNSSocket.on("stop typing", onStopTyping);
        mNSSocket.on("msgUpdate", onNewLog);
        mNSSocket.connect();*/

        connectSocket(mSocket);
        //mSocket.connect();



        Log.d(TAG, "reset socket to NS " + ns);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();

        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("new message", onNewMessage);
        mSocket.off("user joined", onUserJoined);
        mSocket.off("user left", onUserLeft);
        mSocket.off("typing", onTyping);
        mSocket.off("stop typing", onStopTyping);
        mSocket.off("msgUpdate", onNewLog);


    /*
        mNSSocket.disconnect();

        mNSSocket.off(Socket.EVENT_CONNECT, onConnect);
        mNSSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mNSSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mNSSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mNSSocket.off("new message", onNewMessage);
        mNSSocket.off("user joined", onUserJoined);
        mNSSocket.off("user left", onUserLeft);
        mNSSocket.off("typing", onTyping);
        mNSSocket.off("stop typing", onStopTyping);
        mNSSocket.off("msgUpdate", onNewLog);

        */



    }

    public void disconnectSocket(Socket socket){

        socket.disconnect();

        socket.off(Socket.EVENT_CONNECT, onConnect);
        socket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        socket.off("new message", onNewMessage);
        socket.off("user joined", onUserJoined);
        socket.off("user left", onUserLeft);
        socket.off("typing", onTyping);
        socket.off("stop typing", onStopTyping);
        socket.off("msgUpdate", onNewLog);

        socket.disconnect();


    }

    public void connectSocket(Socket socket){
        socket.on(Socket.EVENT_CONNECT,onConnect);
        socket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        socket.on("new message", onNewMessage);
        socket.on("user joined", onUserJoined);
        socket.on("user left", onUserLeft);
        socket.on("typing", onTyping);
        socket.on("stop typing", onStopTyping);
        socket.on("msgUpdate", onNewLog);
        socket.connect();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMessagesView = (RecyclerView) view.findViewById(R.id.messages);
        mMessagesView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMessagesView.setAdapter(mAdapter);

        mInputMessageView = (EditText) view.findViewById(R.id.message_input);
        mInputMessageView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == R.id.send || id == EditorInfo.IME_NULL) {
                    attemptSend();
                    return true;
                }
                return false;
            }
        });
        mInputMessageView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null == mUsername) return;
                if (!mSocket.connected()) return;

                if (!mTyping) {
                    mTyping = true;
                    mSocket.emit("typing");
                }

                mTypingHandler.removeCallbacks(onTypingTimeout);
                mTypingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ImageButton sendButton = (ImageButton) view.findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSend();
            }
        });

        addLog(getResources().getString(R.string.message_welcome));
        //Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        //.setSupportActionBar(toolbar);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK != resultCode) {
            getActivity().finish();
            return;
        }

        mUsername = data.getStringExtra("username");
        int numUsers = data.getIntExtra("numUsers", 1);

        addLog(getResources().getString(R.string.message_welcome));
        addParticipantsLog(numUsers);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_leave) {
            leave();
            return true;
        }

        if (id == R.id.action_join1) {
            setNSSocket(1);
            return true;
        }

        if (id == R.id.action_join5) {
            setNSSocket(5);
            return true;
        }

        if (id == R.id.action_join10) {
            setNSSocket(10);
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    private void addLog(String message) {
        mMessages.add(new Message.Builder(Message.TYPE_LOG)
                .message(message).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void addParticipantsLog(int numUsers) {
        addLog(getResources().getQuantityString(R.plurals.message_participants, numUsers, numUsers));
    }

    private void addMessage(String username, String message) {
        mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                .username(username).message(message).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void addTyping(String username) {
        mMessages.add(new Message.Builder(Message.TYPE_ACTION)
                .username(username).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void removeTyping(String username) {
        for (int i = mMessages.size() - 1; i >= 0; i--) {
            Message message = mMessages.get(i);
            if (message.getType() == Message.TYPE_ACTION && message.getUsername().equals(username)) {
                mMessages.remove(i);
                mAdapter.notifyItemRemoved(i);
            }
        }
    }

    private void attemptSend() {
        if (null == mUsername) return;
        if (!mSocket.connected()) return;

        mTyping = false;

        String message = mInputMessageView.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            mInputMessageView.requestFocus();
            return;
        }

        mInputMessageView.setText("");
        addMessage(mUsername, message);

        // perform the sending message attempt.
        //mSocket.emit("new message", message);
        mSocket.emit("new message", message);
    }

    private void startSignIn() {
        mUsername = null;
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    private void leave() {
        mUsername = null;
        mSocket.disconnect();
        mSocket.connect();

        disconnectSocket(mNSSocket);
        connectSocket(mNSSocket);

        startSignIn();
    }

    private void scrollToBottom() {
        mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);
    }


    private Emitter.Listener onNewLog = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //JSONObject data = (JSONObject) args[0];
                    String data = (String) args[0];
                    String username;
                  //  try {
                        Log.d("something", "new logged message");
                        username = data; //.getString("namespace");
                        Log.d("something", "new logged message from namespace "  + username);

                   // } catch (JSONException e) {
                       // Log.d("something bad", "new logged error.... " + e.toString());

                        return;
                   // }

                }
            });
        }
    };

    private Emitter.Listener onNewRoom = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //JSONObject data = (JSONObject) args[0];
                    String data = (String) args[0];
                    String username;
                    //  try {
                    Log.d("something", "new logged room");
                    username = data; //.getString("namespace");
                    Log.d("something", "new logged room is "  + username);

                    // } catch (JSONException e) {
                    // Log.d("something bad", "new logged error.... " + e.toString());

                    mSocket.emit("new room made",username);
                    return;
                    // }

                }
            });
        }
    };


    private Emitter.Listener onNewRoomMade = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //JSONObject data = (JSONObject) args[0];
                    String data = (String) args[0];
                    String username;
                    //  try {

                    username = data; //.getString("namespace");
                    Log.d("something", "new room made by this user!!!! is "  + username);

                    return;
                    // }

                }
            });
        }
    };


    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!isConnected) {
                        if(null!=mUsername)
                            mSocket.emit("add user", mUsername);
                        Toast.makeText(getActivity().getApplicationContext(),
                                R.string.connect, Toast.LENGTH_LONG).show();
                        isConnected = true;
                    }
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isConnected = false;
                    Toast.makeText(getActivity().getApplicationContext(),
                            R.string.disconnect, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity().getApplicationContext(),
                            R.string.error_connect, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }

                    removeTyping(username);
                    addMessage(username, message);
                }
            });
        }
    };

    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        return;
                    }

                    addLog(getResources().getString(R.string.message_user_joined, username));
                    addParticipantsLog(numUsers);
                }
            });
        }
    };

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        return;
                    }

                    addLog(getResources().getString(R.string.message_user_left, username));
                    addParticipantsLog(numUsers);
                    removeTyping(username);
                }
            });
        }
    };

    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    Log.d("tag"," has started typing");

                    try {
                        username = data.getString("username");
                        Log.d("tag",username + " has started typing");

                    } catch (JSONException e) {
                        return;
                    }
                    addTyping(username);
                }
            });
        }
    };

    private Emitter.Listener onStopTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    Log.d("tag", " has started typing");

                    try {
                        username = data.getString("username");
                        Log.d("tag",username + " has stopped typing");
                    } catch (JSONException e) {
                        return;
                    }
                    removeTyping(username);
                }
            });
        }
    };

    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
            if (!mTyping) return;

            mTyping = false;
            mSocket.emit("stop typing");
        }
    };
}

