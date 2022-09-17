package net.adriantodt.apicursos.main

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.EndpointGroup
import net.adriantodt.apicursos.common.model.dao.DataAccessObject
import net.adriantodt.apicursos.main.controller.AdminPanelController
import net.adriantodt.apicursos.main.controller.DashboardController
import net.adriantodt.apicursos.main.controller.LoginController
import net.adriantodt.apicursos.main.controller.ProfileController
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class MainEndpoints(
    override val kodein: Kodein,
    val app: Javalin,
    val serverName: String
) : EndpointGroup, KodeinAware {
    val dao: DataAccessObject by instance()

    override fun addEndpoints() {
        //app.apply {
        //    error(404) {
        //
        //    }
        //    error(403) {
        //
        //    }
        //    error(500) {
        //
        //    }
        //}
        LoginController(kodein, app).addEndpoints()
        path("dashboard") { DashboardController(kodein, app).addEndpoints() }
        path("adminPanel") { AdminPanelController(kodein, app).addEndpoints() }
        path("profile") { ProfileController(kodein, app).addEndpoints() }
    }
}

