/** The `Message` class is a Kotlin class that represents a chat message and contains properties for the
message content and sender ID. */
package com.example.chatapp

class Message {

    var message: String? = null
    var senderId: String? = null

    constructor() {}

    constructor(message: String?, senderId: String?) {
        this.message = message
        this.senderId = senderId
    }
}
