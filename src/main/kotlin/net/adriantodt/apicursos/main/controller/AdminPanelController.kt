package net.adriantodt.apicursos.main.controller

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.apibuilder.EndpointGroup
import io.javalin.core.security.SecurityUtil.roles
import io.javalin.http.Context
import net.adriantodt.apicursos.common.model.Site
import net.adriantodt.apicursos.common.model.User
import net.adriantodt.apicursos.common.model.UserType
import net.adriantodt.apicursos.common.model.UserType.USER
import net.adriantodt.apicursos.common.model.dao.DataAccessObject
import net.adriantodt.apicursos.main.controller.security.MainRole.ADMIN
import net.adriantodt.apicursos.main.view.adminPanel.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class AdminPanelController(override val kodein: Kodein, val app: Javalin) : EndpointGroup, KodeinAware {
    companion object {
        private val adminRole = roles(ADMIN)
    }

    val dao: DataAccessObject by instance()

    override fun addEndpoints() {
        get(::adminPanel, adminRole)

        get("sites", ::adminPanelSites, adminRole)
        get("users", ::adminPanelUsers, adminRole)

        get("sites/:site", ::adminPanelSite, adminRole)
        get("users/:user", ::adminPanelUser, adminRole)

        get("newSite", ::adminPanelNewSite, adminRole)
        post("newSite", ::adminPanelCreateNewSite, adminRole)

        get("newUser", ::adminPanelNewUser, adminRole)
        post("newUser", ::adminPanelCreateNewUser, adminRole)
    }

    private fun adminPanel(ctx: Context) {
        val users = dao.allUsers().sortedBy(User::name)
        val sites = dao.allSites().sortedBy(Site::name).map { it to dao.getUser(it.ownerId) }
        AdminPanelView(sites, users).render(ctx)
    }

    private fun adminPanelSites(ctx: Context) {
        val sites = dao.allSites().sortedBy(Site::name).map { it to dao.getUser(it.ownerId) }
        AdminPanelSitesView(sites).render(ctx)
    }

    private fun adminPanelUsers(ctx: Context) {
        val users = dao.allUsers().sortedBy(User::name)
        AdminPanelUsersView(users).render(ctx)
    }

    private fun adminPanelSite(ctx: Context) {
        val siteId = ctx.pathParam("site").toLong()

        when(ctx.queryParam("action")) {
            "delete" -> {
                dao.removeSite(siteId)
                ctx.redirect("/adminPanel/sites")
            }
            else -> {
                val site = dao.getSite(siteId)!!
                AdminPanelSiteView(site, dao.getUser(site.ownerId)!!, dao.allCourses(siteId)).render(ctx)
            }
        }
    }

    private fun adminPanelUser(ctx: Context) {
        val userId = ctx.pathParam("user").toLong()

        when(ctx.queryParam("action")) {
            "delete" -> {
                dao.removeUser(userId)
                ctx.redirect("/adminPanel/users")
            }
            else -> {
                AdminPanelUserView(dao.getUser(userId)!!, dao.allSites(userId)).render(ctx)
            }
        }
    }

    private fun adminPanelNewSite(ctx: Context) {
        val users = dao.allUsers().filter { it.userType == USER }.sortedBy(User::name)
        val inputUser = ctx.queryParam("u")?.toLong()
        AdminPanelNewSiteView(users, inputUser).render(ctx)
    }

    private fun adminPanelCreateNewSite(ctx: Context) {
        val user = ctx.formParam("user")?.toLongOrNull()
        val siteName = ctx.formParam("siteName")
        val name = ctx.formParam("name")

        if (user != null && !siteName.isNullOrBlank() && !name.isNullOrBlank()) {
            val site = dao.insertSite(siteName, user, name)
            ctx.redirect("/adminPanel/sites/${site.siteId}")
        } else {
            val users = dao.allUsers().filter { it.userType == USER }.sortedBy(User::name)
            AdminPanelNewSiteView(users, user, siteName, name).render(ctx)
        }
    }

    private fun adminPanelNewUser(ctx: Context) {
        AdminPanelNewUserView().render(ctx)
    }

    private fun adminPanelCreateNewUser(ctx: Context) {
        val username = ctx.formParam("username")
        val name = ctx.formParam("name")
        val email = ctx.formParam("email")
        val password = ctx.formParam("password")
        val action = ctx.formParam("action")
        val isAdmin = action == "createAdmin"

        if (!username.isNullOrBlank() && !name.isNullOrBlank() && !email.isNullOrBlank() && !password.isNullOrBlank() && !action.isNullOrBlank()) {
            val user = dao.insertUser(if (isAdmin) UserType.ADMIN else USER, name, username, email, password)

            if (action == "chainToSite") {
                ctx.redirect("/adminPanel/newSite?u=${user.userId}")
            } else {
                ctx.redirect("/adminPanel/users/${user.userId}")
            }
        } else {
            AdminPanelNewUserView(username, name, email).render(ctx)
        }
    }
}

