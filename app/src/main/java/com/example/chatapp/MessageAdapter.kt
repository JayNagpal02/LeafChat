package com.example.chatapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth

//decryption imports
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.io.Serializable
import java.security.Key
import java.security.KeyPairGenerator
import java.security.Security
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class MessageAdapter(private val context: Context, private val messageList: ArrayList<Message>) :
    RecyclerView.Adapter<ViewHolder>() {

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    val aesKey = CryptoUtils.generateAESKey()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if (viewType == 1) {
            // inflate receive
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
            return ReceiveViewHolder(view)
        } else {
            //inflate send
            val view: View = LayoutInflater.from(context).inflate(R.layout.send, parent, false)
            return SentViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentMessage = messageList[position]

        if (holder.javaClass == SentViewHolder::class.java) {
            // do the stuff for sent view holder
            val viewHolder = holder as SentViewHolder
            try {
                val decryptedText =
                    currentMessage.message?.let { CryptoUtils.decryptAES(it.toByteArray(), aesKey) }
                holder.sentMessage.text = decryptedText
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Error decrypting message: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
                holder.sentMessage.text = "Decryption Error"
                e.printStackTrace()
            }

        } else {
            // do the stuff for receive view holder
            val viewHolder = holder as ReceiveViewHolder
            try {
                val decryptedText =
                    currentMessage.message?.let { CryptoUtils.decryptAES(it.toByteArray(), aesKey) }
                holder.receiveMessage.text = decryptedText
            } catch (e: Exception) {
//                Toast.makeText(context, "Error decrypting message: ${e.message}", Toast.LENGTH_SHORT).show()
                holder.receiveMessage.text = "Decryption Error"
                e.printStackTrace()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        val currentMessage = messageList[position]

        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)) {
            return ITEM_SENT
        } else {
            return ITEM_RECEIVE
        }

    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class SentViewHolder(itemView: View) : ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.txt_sent_message)
    }

    class ReceiveViewHolder(itemView: View) : ViewHolder(itemView) {
        val receiveMessage = itemView.findViewById<TextView>(R.id.txt_receive_message)
    }

    private fun to_byte_array(s: String?): Serializable {

        if (s != null) {
            val x = s.toByteArray(Charsets.UTF_8)
            println("to_byte_array : $x")
            return x
        }
        return false;
    }
}
