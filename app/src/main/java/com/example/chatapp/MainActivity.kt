/**
 * The `MainActivity` class is the main activity of a chat app written in Kotlin, which handles user
 * authentication, database operations, and UI setup. The code you provided is the main activity of
 * a chat app written in Kotlin.
 */
package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.security.Security
import org.bouncycastle.jce.provider.BouncyCastleProvider

class MainActivity : AppCompatActivity() {

    /** These are member variables of the `MainActivity` class. */
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    /**
     * The `onCreate` function sets up the activity by initializing Firebase authentication and
     * database references, setting up the user list and adapter, and retrieving user data from the
     * database.
     *
     * @param savedInstanceState The savedInstanceState parameter is a Bundle object that contains
     * the data that was saved in the onSaveInstanceState() method. It is used to restore the
     * activity's previous state, such as the UI state, when the activity is recreated after being
     * destroyed and recreated by the system.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Register the Bouncy Castle provider
        Security.addProvider(BouncyCastleProvider())

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference

        userList = ArrayList()
        adapter = UserAdapter(this, userList)

        userRecyclerView = findViewById(R.id.userRecyclerView)

        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter

        /**
         * The code `mDbRef.child("users").addValueEventListener(object : ValueEventListener { ...
         * })` is adding a ValueEventListener to the "users" node in the Firebase Realtime Database.
         */
        mDbRef.child("users")
                .addValueEventListener(
                        object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                userList.clear()
                                for (postSnapshot in snapshot.children) {
                                    val currentUser = postSnapshot.getValue(User::class.java)
                                    if (mAuth.currentUser?.uid != currentUser?.uid) {
                                        userList.add(currentUser!!)
                                    }
                                }
                                adapter.notifyDataSetChanged()
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        }
                )
    }

    /**
     * The function inflates a menu resource file and adds it to the options menu.
     *
     * @param menu The `menu` parameter is an instance of the `Menu` class. It represents the menu
     * that will be displayed in the activity's action bar, if it has one.
     * @return The method is returning a boolean value.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * This function handles the logic for logging out a user in a Kotlin Android application.
     *
     * @param item The `item` parameter is of type `MenuItem` and represents the menu item that was
     * selected by the user.
     * @return The method is returning a boolean value.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.logout) {
            // write the logic for logout
            mAuth.signOut()
            val intent = Intent(this@MainActivity, Login::class.java)
            finish()
            startActivity(intent)
            return true
        }
        return true
    }
}
