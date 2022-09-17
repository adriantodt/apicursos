package net.adriantodt.apicursos.main.view.adminPanel

import io.javalin.http.Context
import kotlinx.html.*
import net.adriantodt.apicursos.common.model.Site
import net.adriantodt.apicursos.common.model.User
import net.adriantodt.apicursos.common.model.UserType
import net.adriantodt.apicursos.main.view.ApicursosPageView

class AdminPanelView(val sites: List<Pair<Site, User?>>, val users: List<User>) : ApicursosPageView() {
    override val pageTitle: String = "Administração"
    override val pageCss: String? = null
    override val mainPage: String = "/adminPanel"

    override fun DIV.renderPage(ctx: Context) {
        nav {
            ol("breadcrumb") {
                li("breadcrumb-item active") {
                    +"Administração"
                }
            }
        }

        hr()

        div("row") {
            div("col-sm") { collumn1(ctx) }
            div("col-sm") { collumn2(ctx) }
            div("col-sm") { collumn3(ctx) }
        }
    }

    private fun DIV.collumn1(ctx: Context) {
        div("card") {
            div("card-body") {
                h5("card-title") { +"Administração" }
                p("card-text") {
                    val userCount = users.size
                    val siteCount = sites.size
                    +"Atualmente com $userCount usuário"
                    if (userCount > 1) +"s"
                    +" e $siteCount site"
                    if (siteCount > 1) +"s"
                    +" no sistema."
                }
                a("/adminPanel/users", classes = "btn btn-info btn-block") {
                    +"Usuários"
                }
                a("/adminPanel/newUser", classes = "btn btn-success btn-block") {
                    +"Novo usuário"
                }
                hr()
                a("/adminPanel/sites", classes = "btn btn-info btn-block") {
                    +"Sites"
                }
                a("/adminPanel/newSite", classes = "btn btn-success btn-block") {
                    +"Novo site"
                }
            }
        }

        br
    }

    private fun DIV.collumn2(ctx: Context) {

        div("card") {
            div("card-body") {
                div("row") {
                    div("col") {
                        h5("card-title mb-0") { +"Usuários recentes" }
                    }
                    div("col-auto") {
                        a("/adminPanel/newUser", classes = "btn btn-sm btn-success btn-block") { +"+" }
                    }
                }
            }
            if (users.isNotEmpty()) {
                ul("list-group list-group-flush") {
                    for (user in users) {
                        li("list-group-item") {
                            a("/adminPanel/users/${user.userId}") {
                                +user.name
                                br
                                small {
                                    +when(user.userType) {
                                        UserType.USER -> "Usuário"
                                        UserType.ADMIN -> "Administrador"
                                    }
                                    if (ctx.sessionAttribute<Long>("USER_ID") == user.userId) {
                                        +" (Você)"
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        br
    }

    private fun DIV.collumn3(ctx: Context) {
        div("card") {
            div("card-body") {
                div("row") {
                    div("col") {
                        h5("card-title mb-0") { +"Sites recentes" }
                    }
                    div("col-auto") {
                        a("/adminPanel/newSite", classes = "btn btn-sm btn-success btn-block") { +"+" }
                    }
                }

            }
            if (sites.isNotEmpty()) {
                ul("list-group list-group-flush") {
                    for ((site, user) in sites) {
                        li("list-group-item") {
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
                    }
                }
            }
        }

        br
    }
}