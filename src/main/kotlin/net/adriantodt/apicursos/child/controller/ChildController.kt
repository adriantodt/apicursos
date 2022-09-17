package net.adriantodt.apicursos.child.controller

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.apibuilder.EndpointGroup
import io.javalin.http.Context
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class ChildController(override val kodein: Kodein, val app: Javalin, serverName: String) : EndpointGroup, KodeinAware {
    val serverName = serverName.removePrefix("www.")

    override fun addEndpoints() {
        get("", ::index)
        get(":curso", ::curso)
        get(":curso/public/:resource", ::resource)
        post(":curso/register", ::registerCurso)
        get(":curso/content", ::contentCurso)
    }

    val Context.normalizedName: String
        get() = req.serverName.removeSurrounding("www.",".$serverName")

    fun index(ctx: Context) {

    }

    fun curso(ctx: Context) {

    }

    fun resource(ctx: Context) {

    }

    fun registerCurso(ctx: Context) {

    }

    fun contentCurso(ctx: Context) {

    }
}
