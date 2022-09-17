package net.adriantodt.apicursos.main.view.adminPanel

import io.javalin.http.Context
import kotlinx.html.*
import kotlinx.html.ButtonType.button
import net.adriantodt.apicursos.common.model.Site
import net.adriantodt.apicursos.common.model.User
import net.adriantodt.apicursos.common.model.dao.DataAccessObject
import net.adriantodt.apicursos.main.view.ApicursosPageView

class AdminPanelSitesView(val sites: List<Pair<Site, User?>>) : ApicursosPageView() {
    override val pageTitle: String = "Sites"
    override val pageCss: String? = null
    override val mainPage: String = "/adminPanel"

    override fun DIV.renderPage(ctx: Context) {

        div("row") {
            div("col") {
                nav {
                    ol("breadcrumb") {
                        li("breadcrumb-item") {
                            a(mainPage) {
                                +"Administração"
                            }
                        }
                        li("breadcrumb-item active") {
                            +"Sites"
                        }
                    }
                }
            }
            div("col-auto") {
                a("/adminPanel/newSite", classes = "btn btn-lg btn-success") { +"+" }
            }
        }

        hr()
        if (sites.isNotEmpty()) {
            div("card") {
                ul("list-group list-group-flush") {
                    for ((site, user) in sites) {
                        li("list-group-item") {
                            div("row") {
                                div("col") {
                                    a("/adminPanel/sites/${site.siteId}") {
                                        +site.name
                                        br
                                        small {
                                            +(user?.name ?: "Usuário Inexistente")
                                            +" - "
                                            +site.hostname
                                        }
                                    }
                                }
                                div("col-auto") {
                                    div("btn-group") {
                                        a("/adminPanel/sites/${site.siteId}", classes = "btn btn-info") {
                                            +"Gerenciar"
                                        }
                                        a("/adminPanel/sites/${site.hostname}?action=delete", classes = "btn btn-danger") {
                                            +"Excluir"
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}