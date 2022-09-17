package net.adriantodt.apicursos.main.controller

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.apibuilder.EndpointGroup
import io.javalin.core.security.SecurityUtil.roles
import io.javalin.http.Context
import net.adriantodt.apicursos.common.model.dao.DataAccessObject
import net.adriantodt.apicursos.main.controller.security.MainRole.*
import net.adriantodt.apicursos.main.view.profile.ProfileView
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class ProfileController(override val kodein: Kodein, val app: Javalin) : EndpointGroup, KodeinAware {
    companion object {
        private val userRole = roles(USER, ADMIN)
    }

    val dao: DataAccessObject by instance()

    override fun addEndpoints() {
        get(::profile, userRole)
        post(::updateProfile, userRole)
    }

    private fun profile(ctx: Context) {
        ProfileView().render(ctx)
    }

    private fun updateProfile(ctx: Context) {

    }

}

