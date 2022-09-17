package net.adriantodt.apicursos.common.model

data class Site(
    val siteId: Long,

    var hostname: String,
    var ownerId: Long,

    var name: String,
    var theme: String,
    var hasLogo: Boolean = false
)