package net.adriantodt.apicursos.common.view

import io.javalin.http.Context
import kotlinx.html.HTML
import kotlinx.html.html
import kotlinx.html.stream.appendHTML

abstract class HtmlBuilderView : View {
    abstract fun HTML.render(ctx: Context)

    override fun render(ctx: Context) {
        ctx.contentType("text/html").resultHTML().html { render(ctx) }
    }

    private fun Context.resultHTML() = res.writer.appendHTML()
}