package com.example.chatapp

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Key
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.Security
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object CryptoUtils {
    init {
        Security.addProvider(BouncyCastleProvider()) // Register Bouncy Castle provider
    }

    private const val AES_KEY_SIZE = 256
    private const val AES_ALGORITHM = "AES"
    private const val AES_TRANSFORMATION_CBC = "AES/CBC/PKCS7Padding"
    private const val AES_TRANSFORMATION_GCM = "AES/GCM/NoPadding"
    private const val GCM_IV_LENGTH = 12

    fun generateAESKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM)
        keyGenerator.init(AES_KEY_SIZE)
        return keyGenerator.generateKey()
    }

    fun encryptAES(message: String, key: Key): ByteArray {
        val cipher = Cipher.getInstance(AES_TRANSFORMATION_CBC)
        val iv = generateRandomIV(cipher.blockSize)
        cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(iv))
        val encryptedData = cipher.doFinal(message.toByteArray())
        return iv + encryptedData
    }

    fun decryptAES(encryptedData: ByteArray, key: Key): String {
        val cipher = Cipher.getInstance(AES_TRANSFORMATION_CBC)
        val iv = encryptedData.copyOfRange(0, cipher.blockSize)
        val cipherData = encryptedData.copyOfRange(cipher.blockSize, encryptedData.size)
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))
        val decryptedData = cipher.doFinal(cipherData)
        return String(decryptedData)
    }

    private fun generateRandomIV(length: Int): ByteArray {
        val random = SecureRandom()
        val iv = ByteArray(length)
        random.nextBytes(iv)
        return iv
    }
}
