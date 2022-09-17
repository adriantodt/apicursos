package net.adriantodt.apicursos.child

import java.io.InputStream

object ChildStaticContent {
    fun getStaticContent(location: String): InputStream? {
        return ChildStaticContent::class.java.getResourceAsStream(location)
    }
}