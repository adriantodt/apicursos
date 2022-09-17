package net.adriantodt.apicursos.child.view.newspaper

import io.javalin.http.Context
import kotlinx.html.*
import net.adriantodt.apicursos.child.view.base.BootstrapFlavour
import net.adriantodt.apicursos.common.model.Course
import net.adriantodt.apicursos.common.model.Site
import net.adriantodt.apicursos.common.view.HtmlBuilderView

abstract class NewspaperPageView(val bootstrap: BootstrapFlavour,val site: Site) : HtmlBuilderView() {
    abstract val pageTitle: String
    abstract val pageCss: String?

    abstract fun DIV.renderPage(ctx: Context)

    final override fun HTML.render(ctx: Context) {
        head {
            meta(charset = "utf-8")
            meta("viewport", "width=device-width, initial-scale=1, shrink-to-fit=no")
            meta("description", pageTitle)
            meta("author", site.name)
            title("${site.name} · $pageTitle")
            link(bootstrap.stylesheet, "stylesheet")
            pageCss?.let { link(it, "stylesheet") }
        }
        body {
            div("container") {
                header("blog-header py-3") {
                    div("row flex-nowrap justify-content-between align-items-center") {
                        div("col-4")
                        div("col-4 text-center") {
                            a("/", classes = "text-muted") { +site.name }
                        }
                        div("col-4")
                    }
                }
                /*
                    <div class="nav-scroller py-1 mb-2">
        <nav class="nav d-flex justify-content-between">
            <a class="p-2 text-muted" href="#">World</a>
            <a class="p-2 text-muted" href="#">U.S.</a>
            <a class="p-2 text-muted" href="#">Technology</a>
            <a class="p-2 text-muted" href="#">Design</a>
            <a class="p-2 text-muted" href="#">Culture</a>
            <a class="p-2 text-muted" href="#">Business</a>
            <a class="p-2 text-muted" href="#">Politics</a>
            <a class="p-2 text-muted" href="#">Opinion</a>
            <a class="p-2 text-muted" href="#">Science</a>
            <a class="p-2 text-muted" href="#">Health</a>
            <a class="p-2 text-muted" href="#">Style</a>
            <a class="p-2 text-muted" href="#">Travel</a>
        </nav>
    </div>
                 */

            }
            script(src = "https://code.jquery.com/jquery-3.3.1.slim.min.js") {}
            script(src = "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js") {}
            script(src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js") {}
        }
    }
}
