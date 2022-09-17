package net.adriantodt.apicursos.main.controller

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.apibuilder.EndpointGroup
import io.javalin.core.security.SecurityUtil.roles
import io.javalin.http.Context
import net.adriantodt.apicursos.common.model.dao.DataAccessObject
import net.adriantodt.apicursos.main.controller.security.MainRole.USER
import net.adriantodt.apicursos.main.view.dashboard.DashboardView
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class DashboardController(override val kodein: Kodein, val app: Javalin) : EndpointGroup, KodeinAware {
    companion object {
        private val userRole = roles(USER)
    }

    val dao: DataAccessObject by instance()

    override fun addEndpoints() {
        get(::dashboard, userRole)

        get("sites/:site", ::dashboardSite, userRole)

        get("newContent", ::dashboardNewContent, userRole)
        post("newContent", ::dashboardCreateNewContent, userRole)
    }

    private fun dashboard(ctx: Context) {
        val sites = dao.allSites(ctx.sessionAttribute("USER_ID")!!)
        val noSitePages = sites.flatMap { dao.allCourses(it.siteId) }.isEmpty()
        DashboardView(
            sites, noSitePages
        ).render(ctx)
    }

    private fun dashboardSite(ctx: Context) {
        val site = ctx.pathParam("site")
    }

    private fun dashboardNewContent(ctx: Context) {

    }

    private fun dashboardCreateNewContent(ctx: Context) {

    }

}

