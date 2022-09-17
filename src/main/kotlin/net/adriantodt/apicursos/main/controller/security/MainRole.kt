package net.adriantodt.apicursos.main.controller.security

import io.javalin.core.security.Role

enum class MainRole : Role {
    ANYONE, USER, ADMIN;
}