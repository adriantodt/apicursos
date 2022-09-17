package net.adriantodt.apicursos.main.view.adminPanel

import io.javalin.http.Context
import kotlinx.html.*
import net.adriantodt.apicursos.common.model.Course
import net.adriantodt.apicursos.common.model.Site
import net.adriantodt.apicursos.common.model.User
import net.adriantodt.apicursos.main.view.ApicursosPageView

class AdminPanelSiteView(val site: Site, val owner: User, val courses: List<Course>) : ApicursosPageView() {
    override val pageTitle: String = site.name
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
                            a("$mainPage/site") {
                                +"Sites"
                            }
                        }
                        li("breadcrumb-item active") {
                            +owner.name
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
                        h4("card-title") { +site.name }
                        h5("card-subtitle mb-2 text-muted") { +"${owner.name} / ${site.hostname}" }

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
                                    value = owner.username
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
                                    value = owner.name
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
                                    value = owner.email
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
                                if (ctx.sessionAttribute<Long>("USER_ID") != owner.userId) {
                                    a("/adminPanel/users/${owner.userId}?action=delete", classes = "btn btn-danger") {
                                        +"Excluir"
                                    }
                                }
                            }
                        }
                    }
                }
            }
            div("col-sm") {
                if (courses.isNotEmpty()) {
                    div("card") {
                        div("card-body") {
                            h5("card-title mb-0") { +"Cursos" }
                        }
                        ul("list-group list-group-flush") {
                            for (course in courses) {
                                li("list-group-item") {
                                    div("row") {
                                        div("col") {
                                            a("/adminPanel/courses/${course.courseId}/") {
                                                +course.title
                                                br
                                                small { +"${site.hostname}/courses/${course.url}" }
                                            }
                                        }
                                        div("col-auto") {
                                            div("btn-group btn-group-sm") {
                                                a("/adminPanel/courses/${course.courseId}", classes = "btn btn-info") {
                                                    +"Gerenciar"
                                                }
                                                a("/adminPanel/sites/${course.courseId}?action=delete", classes = "btn btn-danger") {
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
            div("col-sm") {

            }
        }
    }
}