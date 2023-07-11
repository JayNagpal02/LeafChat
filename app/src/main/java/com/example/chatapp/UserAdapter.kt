/**
 * The `UserAdapter` class is a RecyclerView adapter that binds user data to views and handles click
 * events to start a chat activity. The code is importing necessary classes and packages for the
 * `UserAdapter` class in the `com.example.chatapp` package.
 */
package com.example.chatapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(val context: Context, val userList: ArrayList<User>) :
        RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    /**
     * The function creates and returns a UserViewHolder object by inflating a user_layout view.
     *
     * @param parent The parent parameter is the ViewGroup that the ViewHolder will be attached to.
     * It represents the parent ViewGroup of the item views in the RecyclerView.
     * @param viewType The viewType parameter is an integer value that represents the type of view
     * that will be created. It can be used to create different types of views within the same
     * RecyclerView, such as different layouts or view holders.
     * @return an instance of the UserViewHolder class.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        // these are the concepts of recycler view
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view)
    }

    /**
     * This function binds the data of a user to a view holder and sets an onClickListener to start
     * a chat activity with the selected user.
     *
     * @param holder UserViewHolder - The ViewHolder object that holds the views for each item in
     * the RecyclerView.
     * @param position The position parameter represents the position of the item in the
     * RecyclerView that needs to be bound to the ViewHolder. It is used to retrieve the
     * corresponding data from the userList.
     */
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        val currentUser = userList[position]

        holder.textName.text = currentUser.name

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)

            intent.putExtra("name", currentUser.name)
            intent.putExtra("uid", currentUser.uid)

            context.startActivity(intent)
        }
    }

    /**
     * The function returns the size of the userList.
     *
     * @return The size of the userList.
     */
    override fun getItemCount(): Int {
        return userList.size
    }

    /**
     * The UserViewHolder class is a RecyclerView.ViewHolder subclass that holds a TextView for
     * displaying a user's name.
     */
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName = itemView.findViewById<TextView>(R.id.txt_name)
    }
}
