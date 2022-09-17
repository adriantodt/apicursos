package net.adriantodt.apicursos.child

import io.javalin.Javalin
import io.javalin.apibuilder.EndpointGroup
import net.adriantodt.apicursos.child.controller.ChildController
import net.adriantodt.apicursos.common.model.dao.DataAccessObject
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class ChildEndpoints(
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
        ChildController(kodein, app, serverName).addEndpoints()
    }
}

