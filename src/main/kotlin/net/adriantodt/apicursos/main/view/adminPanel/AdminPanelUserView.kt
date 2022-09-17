package net.adriantodt.apicursos.main.view.adminPanel

import io.javalin.http.Context
import kotlinx.html.*
import net.adriantodt.apicursos.common.model.Site
import net.adriantodt.apicursos.common.model.User
import net.adriantodt.apicursos.common.model.UserType
import net.adriantodt.apicursos.main.view.ApicursosPageView

class AdminPanelUserView(val user: User, val sites: List<Site>) : ApicursosPageView() {
    override val pageTitle: String = user.name
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
                        li("breadcrumb-item") {
                            a("$mainPage/user") {
                                +"Usuários"
                            }
                        }
                        li("breadcrumb-item active") {
                            +user.name
                        }
                    }
                }
            }
        }

        hr()

        div("row") {
            div("col-sm") {
                div("card") {
                    div("card-body") {
                        h4("card-title") { +user.name }
                        h5("card-subtitle mb-2 text-muted") { +"${user.username} / ${user.email}" }

                        hr()

                        form(method = FormMethod.post) {
                            div("form-group") {
                                label {
                                    htmlFor = "inputUsername"
                                    +"Nome de usuário"
                                }
                                input(type = InputType.text, name = "username", classes = "form-control form-control-sm") {
                                    id = "inputUsername"
                                    required = true
                                    autoFocus = true
                                    placeholder = "Nome de usuário"
                                    value = user.username
                                }
                            }
                            div("form-group") {
                                label {
                                    htmlFor = "inputName"
                                    +"Nome"
                                }
                                input(type = InputType.text, name = "name", classes = "form-control form-control-sm") {
                                    id = "inputName"
                                    required = true
                                    placeholder = "Nome"
                                    value = user.name
                                }
                            }
                            div("form-group") {
                                label {
                                    htmlFor = "inputEmail"
                                    +"E-mail"
                                }
                                input(type = InputType.email, name = "email", classes = "form-control form-control-sm") {
                                    id = "inputEmail"
                                    required = true
                                    placeholder = "E-mail"
                                    value = user.email
                                }
                            }
                            div("form-group") {
                                label {
                                    htmlFor = "inputPassword"
                                    +"Senha"
                                }
                                input(type = InputType.password, name = "password", classes = "form-control form-control-sm") {
                                    id = "inputPassword"
                                    placeholder = "Senha"
                                }
                            }
                            div("btn-group btn-group-sm") {
                                role = "group"
                                button(type = ButtonType.submit, classes = "btn btn-success") {
                                    +"Salvar alterações"
                                }
                                if (ctx.sessionAttribute<Long>("USER_ID") != user.userId) {
                                    a("/adminPanel/users/${user.userId}?action=delete", classes = "btn btn-danger") {
                                        +"Excluir"
                                    }
                                }
                            }
                        }
                    }
                }
            }
            div("col-sm") {
                if (user.userType != UserType.ADMIN) {
                    div("card") {
                        div("card-body") {
                            h5("card-title mb-0") { +"Sites" }
                        }
                        if (sites.isNotEmpty()) {
                            ul("list-group list-group-flush") {
                                for (site in sites) {
                                    li("list-group-item") {
                                        div("row") {
                                            div("col") {
                                                a("/adminPanel/sites/${site.siteId}") {
                                                    +site.name
                                                    br
                                                    small { +site.hostname }
                                                }
                                            }
                                            div("col-auto") {
                                                div("btn-group btn-group-sm") {
                                                    a("/adminPanel/sites/${site.siteId}", classes = "btn btn-info") {
                                                        +"Gerenciar"
                                                    }
                                                    a("/adminPanel/sites/${site.siteId}?action=delete", classes = "btn btn-danger") {
                                                        +"Excluir"
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        div("card-body") {
                            a("/adminPanel/newSite?u=${user.userId}", classes = "btn btn-success btn-sm btn-block") {
                                +"Novo site"
                            }
                        }
                    }
                }
            }
            div("col-sm") {

            }
        }
    }
}