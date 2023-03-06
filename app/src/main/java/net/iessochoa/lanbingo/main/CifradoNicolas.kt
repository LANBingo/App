package net.iessochoa.lanbingo.main

import kotlin.random.Random

object CifradoNicolas {
    fun cifrador(pass: String): String {
        val rng = Random
        val chars = pass.toCharArray()
        val cesar = rng.nextInt(1, 9)
        val dir = rng.nextBoolean()
        for (i in chars.indices) {
            var c = chars[i]
            if (dir) c += cesar else c -= cesar
            chars[i] = c
        }
        return if (dir) "+" + String(chars) + cesar + String(chars) + cesar + String(chars) + cesar
        else "-" + String(chars) + cesar + String(chars) + cesar + String(chars) + cesar
    }

    fun descifrador(cifrado: String): String {
        val dir: Boolean = cifrado[0] == '+'
        var ciph = cifrado.substring(1)
        if(ciph.substring(0, ciph.length/3) != ciph.substring(ciph.length/3, (ciph.length/3)*2) ||
            ciph.substring(ciph.length/3, (ciph.length/3)*2) != ciph.substring((ciph.length/3)*2) ||
            ciph.substring(0, ciph.length/3) != ciph.substring((ciph.length/3)*2)){
            return "Error"
        }
        val cesar = ciph.substring(ciph.length - 1).toInt()
        ciph = ciph.substring(0, ciph.length - 1)
        val chars = ciph.toCharArray()
        for (i in chars.indices) {
            var c = chars[i]
            if (dir) c -= cesar else c += cesar
            chars[i] = c
        }
        return String(chars)
    }
}