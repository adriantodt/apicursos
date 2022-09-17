package net.adriantodt.apicursos.common.model.dao

import net.adriantodt.apicursos.common.model.*

fun DataAccessObject.insertSampleData() = apply {
    insertUser(
        UserType.ADMIN,
        "Adrian (Admin)",
        "adrian.admin",
        "adrian@localhost",
        "password"
    )

    insertSite(
        "adriantodt.localhost",
        insertUser(
            UserType.USER,
            "Adrian Todt",
            "adriantodt",
            "adriantodt@localhost",
            "password"
        ).userId,
        "AdrianTodt Cursos"
    )

    insertSite(
        "lova.localhost",
        insertUser(
            UserType.USER,
            "Lorena Todt",
            "lorena",
            "lorena@localhost",
            "password"
        ).userId,
        "LoVa Cursos"
    )
}
