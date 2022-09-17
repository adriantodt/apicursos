package net.adriantodt.apicursos.main.view.profile

import io.javalin.http.Context
import io.javalin.http.UnauthorizedResponse
import kotlinx.html.*
import net.adriantodt.apicursos.main.controller.security.MainRole
import net.adriantodt.apicursos.main.view.ApicursosPageView

class ProfileView : ApicursosPageView() {
    override val pageTitle: String = "Perfil"
    override val pageCss: String? = null
    override lateinit var mainPage: String
    lateinit var userRole: MainRole

    override fun render(ctx: Context) {
        userRole = ctx.sessionAttribute<MainRole>("USER_ROLE")
            ?.takeUnless { it == MainRole.ANYONE }
            ?: throw UnauthorizedResponse()

        mainPage = when (userRole) {
            MainRole.USER -> "/dashboard"
            MainRole.ADMIN -> "/adminPanel"
            else -> "/"
        }

        super.render(ctx)
    }

    override fun DIV.renderPage(ctx: Context) {
        nav {
            ol("breadcrumb") {
                li("breadcrumb-item") {
                    a(mainPage) {
                        when (userRole) {
                            MainRole.ANYONE -> +"Início"
                            MainRole.USER -> +"Painel de controle"
                            MainRole.ADMIN -> +"Administração"
                        }
                    }
                }
                li("breadcrumb-item active") { +"Perfil" }
            }
        }

        hr()
    }
}