/* The User class represents a user in a chat app and stores their name, email, and unique identifier. */
package com.example.chatapp

class User {
    var name: String? = null
    var email: String? = null
    var uid: String? = null

    constructor() {}

    constructor(name: String?, email: String?, uid: String?) {
        this.name = name
        this.email = email
        this.uid = uid
    }
}
