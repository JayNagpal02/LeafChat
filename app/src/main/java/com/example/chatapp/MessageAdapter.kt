/** The code is importing necessary classes and packages for the `MessageAdapter` class. */
package com.example.chatapp

/**
 * The `//decryption imports` section is importing necessary classes and packages for encryption and
 * decryption operations in the `MessageAdapter` class.
 */
// decryption imports
import android.content.Context
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth
import javax.crypto.SecretKey

class MessageAdapter(
    private val context: Context,
    private val messageList: ArrayList<Message>
) : RecyclerView.Adapter<ViewHolder>() {

    /**
     * In the `MessageAdapter` class, `ITEM_RECEIVE` and `ITEM_SENT` are constants that are used to
     * determine the view type of each item in the RecyclerView.
     */
    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    /**
     * The function returns a ViewHolder based on the viewType, either a ReceiveViewHolder or a
     * SentViewHolder.
     *
     * @param parent The parent parameter is the ViewGroup that will contain the inflated view. It
     * is typically the RecyclerView that the ViewHolder will be added to.
     * @param viewType The viewType parameter is an integer value that represents the type of view
     * that needs to be created. In this case, if the viewType is 1, it means that a receive view
     * needs to be created. Otherwise, a send view needs to be created.
     * @return an instance of the ViewHolder class. The specific subclass of ViewHolder being
     * returned depends on the value of the viewType parameter. If viewType is 1, an instance of
     * ReceiveViewHolder is returned. Otherwise, an instance of SentViewHolder is returned.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == 1) {
            // inflate receive
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
            return ReceiveViewHolder(view)
        } else {
            // inflate send
            val view: View = LayoutInflater.from(context).inflate(R.layout.send, parent, false)
            return SentViewHolder(view)
        }
    }

    /**
     * This function is responsible for binding data to the view holders in a RecyclerView,
     * decrypting the message if necessary.
     *
     * @param holder The `holder` parameter is an instance of the `ViewHolder` class. It represents
     * the view holder object that should be updated with the data at the specified position in the
     * `messageList`.
     * @param position The `position` parameter represents the position of the item in the
     * `messageList` that needs to be bound to the `ViewHolder`. It is used to retrieve the
     * corresponding message from the list.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // TODO: handle decryption of the message (if necessary instead of binding to the view
        // holder object directly from)
        val currentMessage = messageList[position]
        if (holder.javaClass == SentViewHolder::class.java) {
            // do the stuff for sent view holder
            val viewHolder = holder as SentViewHolder
            val x = currentMessage.message
            val y = decryptMessage(x, 3)

            holder.sentMessage.text = y
        } else {
            // do the stuff for receive view holder
            val viewHolder = holder as ReceiveViewHolder
            val x = currentMessage.message
            val y = decryptMessage(x, 3)
            holder.receiveMessage.text = y
        }
    }

    /**
     * The function returns the view type of an item in a list based on whether the current user is
     * the sender or receiver of the message.
     *
     * @param position The position parameter represents the position of the item in the list for
     * which you want to determine the view type.
     * @return either ITEM_SENT or ITEM_RECEIVE based on the condition.
     */
    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)) {
            return ITEM_SENT
        } else {
            return ITEM_RECEIVE
        }
    }

    /**
     * The function returns the size of the messageList.
     *
     * @return The size of the messageList.
     */
    override fun getItemCount(): Int {
        return messageList.size
    }

    /**
     * The class SentViewHolder is a ViewHolder subclass in Kotlin that holds a reference to a
     * TextView for displaying sent messages.
     */
    class SentViewHolder(itemView: View) : ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.txt_sent_message)
    }

    /**
     * The ReceiveViewHolder class is a Kotlin class that extends the ViewHolder class and holds a
     * TextView for receiving messages.
     */
    class ReceiveViewHolder(itemView: View) : ViewHolder(itemView) {
        val receiveMessage = itemView.findViewById<TextView>(R.id.txt_receive_message)
    }

    fun decryptMessage(message: String?, shift: Int): String {
        val decryptedMessage = StringBuilder()

        if (message != null) {
            for (char in message) {
                if (char.isLetter()) {
                    val base = if (char.isLowerCase()) 'a'.toInt() else 'A'.toInt()
                    val decryptedChar = ((char.toInt() - base - shift + 26) % 26 + base).toChar()
                    decryptedMessage.append(decryptedChar)
                } else {
                    decryptedMessage.append(char)
                }
            }
        }

        return decryptedMessage.toString()
    }
}
