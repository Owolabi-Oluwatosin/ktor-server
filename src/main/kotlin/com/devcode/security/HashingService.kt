package com.devcode.security

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private val ALGORITHM = System.getenv("hash.algorithm")
private val SORT = System.getenv("hash.sorting")
private val HASH_KEY = System.getenv("hash.secret")
private val combinedSalt = "$SORT$HASH_KEY".toByteArray()
private val hMacKey = SecretKeySpec(combinedSalt, ALGORITHM)

fun hashPassword(password: String): String {
    val hMac = Mac.getInstance(ALGORITHM)
    hMac.init(hMacKey)
    return hex(hMac.doFinal(password.toByteArray()))
}