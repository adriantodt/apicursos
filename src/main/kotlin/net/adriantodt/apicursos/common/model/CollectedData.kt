package net.adriantodt.apicursos.common.model

data class CollectedData(
    val dataId: Long,

    val siteId: Long,
    val courseId: Long?,

    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null
)