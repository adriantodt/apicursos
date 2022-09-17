package net.adriantodt.apicursos.main

import java.io.InputStream

object MainStaticContent {
    fun getStaticContent(location: String): InputStream? {
        return MainStaticContent::class.java.getResourceAsStream(location)
    }
}