package net.adriantodt.apicursos

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import mu.KotlinLogging.logger
import net.adriantodt.apicursos.child.ChildEndpoints
import net.adriantodt.apicursos.common.model.dao.DataAccessObject
import net.adriantodt.apicursos.common.model.dao.JdbiDataAccessObject
import net.adriantodt.apicursos.common.model.dao.insertSampleData
import net.adriantodt.apicursos.main.MainEndpoints
import net.adriantodt.apicursos.main.controller.security.MainAccessManager
import org.eclipse.jetty.http.MimeTypes
import org.kodein.di.Kodein
import org.kodein.di.generic.*
import io.javalin.Javalin.create as javalin

private val log = logger {}

fun main() {
    log.info("Hello, World!")

    val serverName = System.getenv("server.name") ?: "localhost"
    val mainPort = System.getenv("port.main")?.toIntOrNull() ?: 8000
    val childPort = System.getenv("port.child")?.toIntOrNull() ?: (mainPort + 1)
    log.info("Server Name: '$serverName'; Ports: $mainPort, $childPort")

    val kodein = Kodein {
        bind<MimeTypes>() with singleton {
            MimeTypes().apply { addMimeMapping("*", "application/octet-stream") }
        }
        bind<Algorithm>() with provider {
            Algorithm.HMAC256(
                "Apicursos was created on 2019 by AdrianTodt"
            )
        }
        bind<JWTVerifier>() with provider {
            JWT.require(instance())
                .withIssuer("apicursos")
                .build()
        }
        bind<DataAccessObject>() with eagerSingleton {
            JdbiDataAccessObject("jdbc:postgresql:apicursos")
                .insertSampleData()
        }
    }
    log.info("Hello, Kodein!")

    mainApp(kodein, serverName, mainPort)
    childApp(kodein, serverName, childPort)

    log.info("Started! \\o/")
}

private fun mainApp(kodein: Kodein, serverName: String, mainPort: Int) {
    log.info("Creating main server...")
    val app = javalin { cfg ->
        cfg.logIfServerNotStarted = false
        cfg.showJavalinBanner = false
        cfg.accessManager(MainAccessManager(kodein))
        cfg.addStaticFiles("net/adriantodt/apicursos/main/public")
    }
    app.routes(MainEndpoints(kodein, app, serverName)).start(mainPort)
}

private fun childApp(kodein: Kodein, serverName: String, childPort: Int) {
    log.info("Creating child server...")
    val app = javalin { cfg ->
        cfg.logIfServerNotStarted = false
        cfg.showJavalinBanner = false
        // cfg.accessManager(mainAccessManager)
        cfg.addStaticFiles("net/adriantodt/apicursos/child/public")
    }
    app.routes(ChildEndpoints(kodein, app, serverName)).start(childPort)
}
