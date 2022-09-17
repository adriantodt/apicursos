package net.adriantodt.apicursos.common.view

import io.javalin.http.Context

interface View {
    fun render(ctx: Context)
}