package net.adriantodt.apicursos.main.controller

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.apibuilder.EndpointGroup
import io.javalin.http.Context
import net.adriantodt.apicursos.common.model.dao.DataAccessObject
import net.adriantodt.apicursos.common.model.User
import net.adriantodt.apicursos.common.model.UserType
import net.adriantodt.apicursos.main.controller.security.MainRole
import net.adriantodt.apicursos.main.controller.security.MainRole.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.direct
import org.kodein.di.generic.instance
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.text.Charsets.UTF_8
import net.adriantodt.apicursos.main.MainStaticContent.getStaticContent as pageContent
import java.net.URLDecoder.decode as urlDecode
import java.net.URLEncoder.encode as urlEncode

class LoginController(override val kodein: Kodein, val app: Javalin) : EndpointGroup, KodeinAware {
    val dao: DataAccessObject by instance()

    override fun addEndpoints() {
        get("login", ::login)
        post("doLogin", ::doLogin)
        get("logout", ::doLogout)
    }

    private fun login(ctx: Context) {
        val then = ctx.queryParam("then")?.let { urlDecode(it, UTF_8) }
        when (ctx.sessionAttribute<MainRole>("USER_ROLE") ?: ANYONE) {
            ANYONE -> {
                val jwtToken = ctx.cookie("ulovecookies")

                if (jwtToken != null) {
                    val verifier: JWTVerifier by instance()

                    verifier.runCatching { verify(jwtToken) }
                        .onFailure { ctx.removeCookie("ulovecookies") }
                        .onSuccess {
                            it.subject.toLongOrNull()?.let(dao::getUser)?.let { u ->
                                loginRoutine(ctx, u, true)
                                return
                            }
                            ctx.removeCookie("ulovecookies")
                        }
                }

                ctx.contentType("text/html").result(pageContent("public/login/index.html")!!)
            }
            USER -> ctx.redirect(then ?: "/dashboard")
            ADMIN -> ctx.redirect(then ?: "/adminPanel")
        }
    }

    private fun doLogin(ctx: Context) {
        val user = ctx.formParam("username")
        val pass = ctx.formParam("password")
        val rememberMe = ctx.formParamMap().containsKey("ilovecookies")

        if (user == null) {
            ctx.redirect("/login/?err=invalidcreds")
            return
        }
        val invalidCredsWithUsername = "/login/?err=invalidcreds&username=${urlEncode(user, UTF_8)}"
        if (pass == null) {
            ctx.redirect(invalidCredsWithUsername)
            return
        }

        dao.getUserByUsernameOrEmail(user)?.let { u ->
            if (DataAccessObject.hashPassword(pass) == u.hashedPassword) {
                loginRoutine(ctx, u, rememberMe)
            }
        }

        ctx.redirect(invalidCredsWithUsername)
    }

    private fun doLogout(ctx: Context) {
        ctx.sessionAttribute("USER_ROLE", ANYONE)
        ctx.removeCookie("ulovecookies")
        ctx.redirect("/")
    }

    private fun loginRoutine(ctx: Context, u: User, rememberMe: Boolean) {
        ctx.sessionAttribute("USER_ID", u.userId)
        ctx.sessionAttribute("USER_NAME", u.username)
        val then = (ctx.formParam("then") ?: ctx.queryParam("then"))?.let { urlDecode(it, UTF_8) }

        if (rememberMe) {
            val expires = Date.from(Instant.now().plus(60, ChronoUnit.DAYS))
            val jwt = JWT.create()
                .withIssuer("apicursos")
                .withSubject(u.userId.toString())
                .withIssuedAt(Date())
                .withExpiresAt(expires)
                .sign(direct.instance())
            ctx.cookie("ulovecookies", jwt, TimeUnit.DAYS.toMillis(60).toInt())
        }
        when (u.userType) {
            UserType.USER -> {
                ctx.sessionAttribute("USER_ROLE", USER)
                ctx.redirect(then ?: "/dashboard")
            }
            UserType.ADMIN -> {
                ctx.sessionAttribute("USER_ROLE", ADMIN)
                ctx.redirect(then ?: "/adminPanel")
            }
        }
    }
}

