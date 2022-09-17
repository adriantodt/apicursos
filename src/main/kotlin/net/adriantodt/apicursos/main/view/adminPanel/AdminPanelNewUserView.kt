package net.adriantodt.apicursos.main.view.adminPanel

import io.javalin.http.Context
import kotlinx.html.*
import kotlinx.html.ButtonType.submit
import kotlinx.html.FormMethod.post
import kotlinx.html.InputType.*
import net.adriantodt.apicursos.common.model.dao.DataAccessObject
import net.adriantodt.apicursos.main.view.ApicursosPageView

class AdminPanelNewUserView(val inputUsername : String? = null, val inputName : String? = null, val inputEmail : String? = null) : ApicursosPageView() {
    override val pageTitle: String = "Novo usuário"
    override val pageCss: String? = null
    override val mainPage: String = "/adminPanel"

    override fun DIV.renderPage(ctx: Context) {

        nav {
            ol("breadcrumb") {
                li("breadcrumb-item") {
                    a(mainPage) {
                        +"Administração"
                    }
                }
                li("breadcrumb-item active") {
                    +"Novo usuário"
                }
            }
        }

        hr()

        form(method = post) {
            div("form-group") {
                label {
                    htmlFor = "inputUsername"
                    +"Nome de usuário"
                }
                input(type = text, name = "username", classes = "form-control") {
                    id = "inputUsername"
                    required = true
                    autoFocus = true
                    placeholder = "Nome de usuário"
                    inputUsername?.let { value = it }
                }
            }
            div("form-group") {
                label {
                    htmlFor = "inputName"
                    +"Nome"
                }
                input(type = text, name = "name", classes = "form-control") {
                    id = "inputName"
                    required = true
                    placeholder = "Nome"
                    inputName?.let { value = it }
                }
            }
            div("form-group") {
                label {
                    htmlFor = "inputEmail"
                    +"E-mail"
                }
                input(type = email, name = "email", classes = "form-control") {
                    id = "inputEmail"
                    required = true
                    placeholder = "E-mail"
                    inputEmail?.let { value = it }
                }
            }
            div("form-group") {
                label {
                    htmlFor = "inputPassword"
                    +"Senha"
                }
                input(type = password, name = "password", classes = "form-control") {
                    id = "inputPassword"
                    required = true
                    placeholder = "Senha"
                }
            }
            div("btn-group") {
                role = "group"
                button(type = submit, name = "action", classes = "btn btn-success") {
                    value = "create"
                    +"Criar usuário"
                }
                button(type = submit, name = "action", classes = "btn btn-warning") {
                    value = "createAdmin"
                    +"Criar administrador"
                }
                button(type = submit, name = "action", classes = "btn btn-primary") {
                    value = "chainToSite"
                    +"Criar usuário e site"
                }
            }
        }
    }
}