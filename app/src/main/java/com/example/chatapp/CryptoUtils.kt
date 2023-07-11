/**
 * The code snippet provided is a Kotlin file that contains a utility class called `CryptoUtils`.
 * This class provides various functions for encryption and decryption using the AES (Advanced
 * Encryption Standard) algorithm. The code snippet you provided is a Kotlin file that contains a
 * utility class called `CryptoUtils`.
 */
package com.example.chatapp

import java.security.Key
import java.security.SecureRandom
import java.security.Security
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import org.bouncycastle.jce.provider.BouncyCastleProvider

object CryptoUtils {
    /**
     * The `init` block in the `CryptoUtils` object is used to initialize the class. In this case,
     * it registers the Bouncy Castle provider as a security provider. The Bouncy Castle provider is
     * a popular open-source cryptographic library that provides various cryptographic algorithms
     * and utilities. By registering the Bouncy Castle provider, the code ensures that it is
     * available for use in the application.
     */
    init {
        Security.addProvider(BouncyCastleProvider()) // Register Bouncy Castle provider
    }

    /**
     * These lines of code define constant values used in the `CryptoUtils` object for AES
     * encryption and decryption.
     */
    private const val AES_KEY_SIZE = 256
    private const val AES_ALGORITHM = "AES"
    private const val AES_TRANSFORMATION_CBC = "AES/CBC/PKCS7Padding"
    private const val AES_TRANSFORMATION_GCM = "AES/GCM/NoPadding"
    private const val GCM_IV_LENGTH = 12

    /**
     * The function generates a secret key for AES encryption.
     *
     * @return The function `generateAESKey()` returns a SecretKey object.
     */
    fun generateAESKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM)
        keyGenerator.init(AES_KEY_SIZE)
        return keyGenerator.generateKey()
    }

    /**
     * The function encrypts a message using AES encryption with a given key and returns the
     * encrypted data.
     *
     * @param message The `message` parameter is the plaintext message that you want to encrypt
     * using AES encryption. It should be a string value.
     * @param key The `key` parameter is an instance of the `Key` class, which represents the secret
     * key used for encryption. The specific type of key depends on the encryption algorithm being
     * used. In this case, it is assumed to be an AES key.
     * @return The method `encryptAES` returns a `ByteArray` which contains the initialization
     * vector (IV) concatenated with the encrypted data.
     */
    fun encryptAES(message: String, key: Key): ByteArray {
        val cipher = Cipher.getInstance(AES_TRANSFORMATION_CBC)
        val iv = generateRandomIV(cipher.blockSize)
        cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(iv))
        val encryptedData = cipher.doFinal(message.toByteArray())
        return iv + encryptedData
    }

    /**
     * The function decrypts AES encrypted data using a given key.
     *
     * @param encryptedData The `encryptedData` parameter is a byte array that contains the data
     * that has been encrypted using the AES algorithm.
     * @param key The `key` parameter is an instance of the `Key` class, which represents the secret
     * key used for encryption and decryption. It should be a valid key for the AES algorithm.
     * @return a decrypted string.
     */
    fun decryptAES(encryptedData: ByteArray, key: Key): String {
        val cipher = Cipher.getInstance(AES_TRANSFORMATION_CBC)
        val iv = encryptedData.copyOfRange(0, cipher.blockSize)
        val cipherData = encryptedData.copyOfRange(cipher.blockSize, encryptedData.size)
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))
        val decryptedData = cipher.doFinal(cipherData)
        return String(decryptedData)
    }

    /**
     * The function generates a random byte array of a specified length.
     *
     * @param length The `length` parameter is an integer that specifies the length of the generated
     * IV (Initialization Vector). The IV is a random value used in cryptographic algorithms to
     * ensure that each encryption operation produces a unique ciphertext, even when encrypting the
     * same plaintext multiple times. The length of the IV depends on the specific
     * @return a randomly generated initialization vector (IV) as a ByteArray.
     */
    private fun generateRandomIV(length: Int): ByteArray {
        val random = SecureRandom()
        val iv = ByteArray(length)
        random.nextBytes(iv)
        return iv
    }
}
