/** The code is importing necessary classes and packages for the ChatApp project. */
package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    /** These lines of code are declaring private properties in the `SignUp` class. */
    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignUp: Button

    /**
     * ```
     *     firebase authentication
     * ```
     */
    private lateinit var mAuth: FirebaseAuth

    /**
     * ```
     *     firebase realtime database
     * ```
     */
    private lateinit var mDbRef: DatabaseReference

    /**
     * The `onCreate` function sets up the sign-up activity by initializing variables, hiding the
     * action bar, and setting a click listener for the sign-up button.
     *
     * @param savedInstanceState The savedInstanceState parameter is used to restore the activity's
     * previous state if it was previously destroyed and recreated by the system. It is a Bundle
     * object that contains the data saved by the onSaveInstanceState() method.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        edtName = findViewById(R.id.edt_name)
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnSignUp = findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            val name = edtName.text.toString()
            signUp(name, email, password)
        }
    }

    /**
     * The signUp function creates a user with the provided name, email, and password, and adds the
     * user to the database, then redirects to the home activity if successful.
     *
     * @param name The name of the user signing up.
     * @param email The email parameter is a string that represents the email address of the user
     * signing up.
     * @param password The password parameter is a String that represents the user's password for
     * signing up.
     */
    private fun signUp(name: String, email: String, password: String) {
        // logic of creating user
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // sign in success,saving data to firebase and redirect to the home activity
                mAuth.currentUser?.sendEmailVerification()
                    ?.addOnSuccessListener {
                        Toast.makeText(
                            this,
                            "Please Verify your Email",
                            Toast.LENGTH_SHORT
                        ).show()
                        addUserToDatabase(
                            name,
                            email,
                            mAuth.currentUser?.uid!!
                        ) // mAuth.currentUser?.uid!! means it is null safe
                    }
                    ?.addOnFailureListener {
                        Toast.makeText(
                            this,
                            it.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                val intent = Intent(this@SignUp, Login::class.java)
                finish()
                startActivity(intent)
            } else {
                Toast.makeText(this@SignUp, "Some error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * The function adds a user to a Firebase database with the provided name, email, and uid.
     *
     * @param name The name of the user to be added to the database.
     * @param email The email parameter is a string that represents the email address of the user.
     * @param uid The "uid" parameter is a unique identifier for the user. It is typically generated
     * by the authentication system and is used to uniquely identify each user in the database.
     */
    private fun addUserToDatabase(name: String, email: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().reference
        mDbRef.child("users").child(uid).setValue(User(name, email, uid))
    }
}
