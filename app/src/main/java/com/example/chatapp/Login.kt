/**
 * The `Login` class handles the login functionality in a Kotlin Android app using Firebase
 * authentication. The `Login` class is responsible for handling the login functionality in a Kotlin
 * Android app using Firebase authentication. The code is importing necessary classes and packages
 * for the ChatApp project.
 */
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

class Login : AppCompatActivity() {

    /** These lines of code are declaring private properties in the `Login` class. */
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button
    private lateinit var role: String

    /**
     * ```
     *     firebase authentication
     * ```
     */
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    /**
     * This function is the onCreate method for the login activity in a Kotlin Android app, which
     * sets up the UI elements and handles the login and sign up button clicks.
     *
     * @param savedInstanceState The savedInstanceState parameter is a Bundle object that contains
     * the data that was saved in the onSaveInstanceState() method. It is used to restore the
     * activity's previous state, such as user input or other data, when the activity is recreated
     * after being destroyed and recreated by the system.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference

        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignUp = findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            login(email, password)
        }
    }

    /**
     * The login function attempts to log in a user with the provided email and password, and if
     * successful, sets the user's role to "Employee" and starts the MainActivity, otherwise it
     * displays a toast message indicating that the user does not exist.
     *
     * @param email The email parameter is a string that represents the user's email address. It is
     * used to identify the user during the login process.
     * @param password The password parameter is a String that represents the user's password.
     */
    private fun login(email: String, password: String) {
        // logic for logging user
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                role = "Employee"
                val intent = Intent(this@Login, MainActivity::class.java)
                finish()
                startActivity(intent)
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(this@Login, "User does not exist", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
