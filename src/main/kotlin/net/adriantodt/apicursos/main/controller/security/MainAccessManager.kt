package net.adriantodt.apicursos.main.controller.security

import io.javalin.core.security.AccessManager
import io.javalin.core.security.Role
import io.javalin.http.Context
import io.javalin.http.Handler
import net.adriantodt.apicursos.main.controller.security.MainRole.ANYONE
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import kotlin.text.Charsets.UTF_8
import java.net.URLEncoder.encode as urlEncode

class MainAccessManager(override val kodein: Kodein) : AccessManager, KodeinAware {
    override fun manage(handler: Handler, ctx: Context, permittedRoles: MutableSet<Role>) {
        if (permittedRoles.isEmpty()) {
            handler.handle(ctx)
        } else {
            val role = ctx.sessionAttribute<MainRole>("USER_ROLE") ?: ANYONE
            if (role in permittedRoles) {
                handler.handle(ctx)
            } else if (role == ANYONE) {
                val thenUrl = listOfNotNull(ctx.path(), ctx.queryString()).joinToString("?")
                ctx.redirect("/login?err=unauthorized&then=${urlEncode(thenUrl, UTF_8)}")
            } else {

            }
        }
    }
}