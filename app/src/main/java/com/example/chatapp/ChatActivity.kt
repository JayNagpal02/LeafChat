/**
 * The `ChatActivity` class in Kotlin represents the chat functionality of a chat application,
 * including sending and receiving messages, encrypting messages, and displaying them in a
 * RecyclerView. The code snippet is a Kotlin class that represents the ChatActivity in a chat
 * application.
 */
package com.example.chatapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Base64
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.crypto.SecretKey

class ChatActivity : AppCompatActivity() {
    /** These are private properties of the `ChatActivity` class in Kotlin. */
    private lateinit var aesKey: SecretKey
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference

    // receiverRoom and senderRoom are used to create a private/unique room between sender and
    // receive so that the messages are private and are not reflect to all the users
    private var receiverRoom: String? = null
    private var senderRoom: String? = null

    /**
     * This function sets up the chat activity, retrieves messages from the database, and sends new
     * messages to the database.
     *
     * @param savedInstanceState The `savedInstanceState` parameter is a Bundle object that contains
     * the data saved from the previous state of the activity. It is used to restore the activity's
     * state when it is recreated, such as after a configuration change (e.g., screen rotation) or
     * when the activity is temporarily destroyed and recreated by
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        // Generate AES key once
        aesKey = CryptoUtils.generateAESKey()

        mDbRef = FirebaseDatabase.getInstance().reference

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = name

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sendButton)
        messageList = ArrayList()

        // Initialize MessageAdapter with aesKey
        messageAdapter = MessageAdapter(this, messageList, aesKey)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        /* The code snippet is adding a `ValueEventListener` to the Firebase Realtime Database reference.
        This listener is triggered whenever there is a change in the data at the specified location in
        the database. */
        // logic for adding data to recyclerView
        mDbRef.child("chats")
                .child(senderRoom!!)
                .child("messages")
                .addValueEventListener(
                        object : ValueEventListener {
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onDataChange(snapshot: DataSnapshot) {
                                messageList.clear()
                                for (postSnapshot in snapshot.children) {
                                    val message = postSnapshot.getValue(Message::class.java)
                                    messageList.add(message!!)
                                }
                                messageAdapter.notifyDataSetChanged()
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        }
                )

        /* The code snippet is adding an `OnClickListener` to the `sendButton` ImageView. When the send
        button is clicked, the code inside the `setOnClickListener` block is executed. */
        // adding the message to database
        sendButton.setOnClickListener {
            val message = messageBox.text.toString()
            // checking for empty message
            if (message != "") {
                // encrypt the message before sending to firebase
                // val encryptedData = CryptoUtils.encryptAES(message,aesKey).toString()
                val encryptedData =
                        Base64.encodeToString(
                                CryptoUtils.encryptAES(message, aesKey),
                                Base64.DEFAULT
                        )
                // creating messageObject
                val messageObject = Message(encryptedData, senderUid)
                // sending messageObject to firebase
                mDbRef.child("chats")
                        .child(senderRoom!!)
                        .child("messages")
                        .push()
                        .setValue((messageObject))
                        .addOnSuccessListener {
                            mDbRef.child("chats")
                                    .child(receiverRoom!!)
                                    .child("messages")
                                    .push()
                                    .setValue((messageObject))
                        }
            }
            // setting messageBox back to empty
            messageBox.setText("")
        }
    }
}
