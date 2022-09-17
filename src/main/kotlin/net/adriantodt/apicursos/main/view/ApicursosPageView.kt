package net.adriantodt.apicursos.main.view

import io.javalin.http.Context
import kotlinx.html.*
import kotlinx.html.ButtonType.button
import net.adriantodt.apicursos.common.view.HtmlBuilderView

abstract class ApicursosPageView : HtmlBuilderView() {
    abstract val pageTitle: String
    abstract val pageCss: String?
    abstract val mainPage: String

    abstract fun DIV.renderPage(ctx: Context)

    final override fun HTML.render(ctx: Context) {
        head {
            meta(charset = "utf-8")
            meta("viewport", "width=device-width, initial-scale=1, shrink-to-fit=no")
            meta("description", "Soluções de Marketing para Captação de Venda de Cursos.")
            meta("author", "Apicursos")
            title("Apicursos · $pageTitle")
            link("https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css", "stylesheet")
            pageCss?.let { link(it, "stylesheet") }
        }
        body("pt-5") {
            header("mb-4") {
                nav("navbar navbar-expand-md navbar-dark fixed-top bg-dark") {
                    a(mainPage, classes = "navbar-brand") {
                        img("", "/img/apicursos.svg") {
                            width = "30"
                            height = "30"
                        }
                        +" Apicursos"
                    }
                    button(type = button, classes = "navbar-toggler") {
                        attributes["data-toggle"] = "collapse"
                        attributes["data-target"] = "#navbarCollapse"
                        span("navbar-toggler-icon")
                    }

                    div("collapse navbar-collapse") {
                        id = "navbarCollapse"
                        ul("navbar-nav ml-auto navbar-right") {
                            li("nav-item") { a("/profile", classes = "nav-link") { +"Perfil" } }
                            li("nav-item") { a("/logout", classes = "nav-link") { +"Sair" } }
                        }
                    }
                }
            }
            main("my-4") {
                role = "main"
                div("container") { renderPage(ctx) }
            }
            footer("container") {
                p("float-right") { a("#") { +"Voltar ao topo" } }
                p {
                    +"© 2019 Apicursos · "
                    a("/logout") { +"Sair" }
                }
            }
            script(src = "https://code.jquery.com/jquery-3.3.1.slim.min.js") {}
            script(src = "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js") {}
            script(src = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js") {}
        }
    }
}