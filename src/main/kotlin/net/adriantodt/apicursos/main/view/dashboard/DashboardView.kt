package net.adriantodt.apicursos.main.view.dashboard

import io.javalin.http.Context
import kotlinx.html.*
import net.adriantodt.apicursos.common.model.Site
import net.adriantodt.apicursos.main.view.ApicursosPageView

class DashboardView(val sites: List<Site>, val noSitePages: Boolean) : ApicursosPageView() {
    override val pageTitle: String = "Painel de controle"
    override val pageCss: String? = null
    override val mainPage: String = "/dashboard"

    override fun DIV.renderPage(ctx: Context) {
        nav {
            ol("breadcrumb") {
                li("breadcrumb-item active") {
                    +"Painel de controle"
                }
            }
        }

        hr()

        div("row") {
            div("col-sm") { collumn11(ctx) }
            div("col-sm") { collumn12(ctx) }
            div("col-sm") { collumn13(ctx) }
        }
        div("row") {
            div("col-sm") { collumn21(ctx) }
            div("col-sm") { collumn22(ctx) }
            div("col-sm") { collumn23(ctx) }
        }
    }

    private fun DIV.collumn11(ctx: Context) {
        div("card") {
            div("card-body") {
                h5("card-title") { +"Bem-vindo" }
                p("card-text") {
                    +"Bem-vindo ao seu Painel de controle."
                }
                p("card-text") {
                    +"Mensagens e Avisos apareceram aqui."
                }
                p("card-text") {
                    +"Aqui você pode gerenciar seu conteúdo."
                }
            }
        }

        br

        div("card") {
            div("card-body") {
                h5("card-title mb-0") { +"Mensagens" }
            }
            ul("list-group list-group-flush") {
                li("list-group-item") {
                    // 'Auto-tutorial'
                    if (sites.isNullOrEmpty()) {
                        +"Você não tem nenhum site. Por favor, "
                        a("https://wa.me/5519994133397?text=Bom%20dia%2C%20estou%20te%20contatando%20quanto%20ao%20Apicursos.") {
                            +"contate-nos via WhatsApp"
                        }
                        +" para começarmos o seu primeiro site."
                    } else if (noSitePages) {
                        +"Você ainda não criou nenhum conteúdo. Clique em "
                        b { +"Novo conteúdo" }
                        +" ao lado para criar e publicar seu primeiro conteúdo."
                    } else {
                        +"Você não tem nenhuma mensagem."
                    }
                }
            }
        }

        br
    }

    private fun DIV.collumn12(ctx: Context) {
        div("card") {
            div("card-body") {
                h5("card-title") { +"Ações Rápidas" }
                a("/dashboard/newContent", classes = "btn btn-success btn-block") {
                    id = "btnNewContent"
                    +"Novo conteúdo"
                }
                a("/dashboard/gatheredData", classes = "btn btn-primary btn-block") {
                    +"Dados colhidos"
                }
            }
        }

        br
    }

    private fun DIV.collumn13(ctx: Context) {
        div("card") {
            div("card-body") {
                h5("card-title") { +"Navegação" }
                a("/dashboard/newContent", classes = "btn btn-info btn-block") {
                    +"Seus sites"
                }
            }
        }

        br

        div("card") {
            div("card-body") {
                h5("card-title") { +"Seus sites" }
                p("card-text") {
                    if (sites.isEmpty()) {
                        a("https://wa.me/5519994133397?text=Bom%20dia%2C%20estou%20te%20contatando%20quanto%20ao%20Apicursos.") {
                            +"Contate-nos via WhatsApp"
                        }
                        +" para começarmos o seu primeiro site."
                    } else {
                        val siteCount = sites.size
                        +"Você tem $siteCount site"
                        if (siteCount > 1) +"s"
                        +" e pode gerenciá-lo"
                        if (siteCount > 1) +"s"
                        +" aqui."
                    }
                }
            }
            if (sites.isNotEmpty()) {
                ul("list-group list-group-flush") {
                    for (site in sites.sortedBy { it.name }) {
                        li("list-group-item") {
                            a("/dashboard/s/${site.siteId}") {
                                +site.name
                                br
                                small { +site.hostname }
                            }
                        }
                    }
                }
            }
        }

        br
    }

    private fun DIV.collumn21(ctx: Context) {
    }

    private fun DIV.collumn22(ctx: Context) {
    }

    private fun DIV.collumn23(ctx: Context) {

    }
}