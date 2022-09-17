package net.adriantodt.apicursos.main.view.adminPanel

import io.javalin.http.Context
import kotlinx.html.*
import kotlinx.html.ButtonType.submit
import kotlinx.html.FormMethod.post
import kotlinx.html.InputType.*
import net.adriantodt.apicursos.common.model.User
import net.adriantodt.apicursos.common.model.dao.DataAccessObject
import net.adriantodt.apicursos.common.model.UserType
import net.adriantodt.apicursos.main.view.ApicursosPageView

class AdminPanelNewSiteView(
    val users: List<User>,
    val inputUser: Long? = null,
    val inputSiteName: String? = null,
    val inputName: String? = null
) : ApicursosPageView() {
    override val pageTitle: String = "Novo site"
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
                    +"Novo site"
                }
            }
        }

        hr()

        form(method = post) {
            div("form-group") {
                label {
                    htmlFor = "inputUser"
                    +"Usuário"
                }
                select("form-control") {
                    name = "user"
                    id = "inputUser"

                    for (user in users) {
                        option {
                            value = user.userId.toString()
                            if (user.userId == inputUser) selected = true
                            +"${user.name} (${user.username})"
                        }
                    }
                }
            }
            div("form-group") {
                label {
                    htmlFor = "inputSiteName"
                    +"URL do site"
                }
                input(type = text, name = "siteName", classes = "form-control") {
                    id = "inputSiteName"
                    required = true
                    placeholder = "URL do site"
                    inputSiteName?.let { value = it }
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
            div("btn-group") {
                role = "group"
                button(type = submit, name = "action", classes = "btn btn-success") {
                    value = "create"
                    +"Criar site"
                }
            }
        }
    }
}