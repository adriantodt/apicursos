package net.adriantodt.apicursos.main.view.adminPanel

import io.javalin.http.Context
import kotlinx.html.*
import kotlinx.html.ButtonType.button
import net.adriantodt.apicursos.common.model.User
import net.adriantodt.apicursos.common.model.dao.DataAccessObject
import net.adriantodt.apicursos.common.model.UserType
import net.adriantodt.apicursos.main.view.ApicursosPageView

class AdminPanelUsersView(val users: List<User>) : ApicursosPageView() {
    override val pageTitle: String = "Usuários"
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
                            +"Usuários"
                        }
                    }
                }
            }
            div("col-auto") {
                a("/adminPanel/newUser", classes = "btn btn-lg btn-success") { +"+" }
            }
        }

        hr()

        if (users.isNotEmpty()) {
            div("card") {
                ul("list-group list-group-flush") {
                    for (user in users) {
                        li("list-group-item") {
                            div("row") {
                                div("col") {
                                    a("/adminPanel/users/${user.userId}") {
                                        +user.name
                                        br
                                        small {
                                            +when (user.userType) {
                                                UserType.USER -> "Usuário"
                                                UserType.ADMIN -> "Administrador"
                                            }
                                            if (ctx.sessionAttribute<Long>("USER_ID") == user.userId) {
                                                +" (Você)"
                                            }
                                        }
                                    }
                                }
                                if (ctx.sessionAttribute<Long>("USER_ID") != user.userId) {
                                    div("col-auto") {
                                        div("btn-group") {
                                            a("/adminPanel/users/${user.userId}", classes = "btn btn-info") {
                                                +"Gerenciar"
                                            }
                                            a("/adminPanel/users/${user.userId}?action=delete", classes = "btn btn-danger") {
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
}