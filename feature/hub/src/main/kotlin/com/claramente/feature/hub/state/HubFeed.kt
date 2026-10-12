package com.claramente.feature.hub.state

object HubFeed {
    val stats: List<HubStat> = listOf(
        HubStat(value = "6 dias", label = "de sequência"),
        HubStat(value = "2", label = "atividades pendentes"),
        HubStat(value = "18 dias", label = "para o simulado"),
    )

    val items: List<FeedItem> = listOf(
        FeedItem(
            id = "1",
            tag = "Aviso",
            tone = FeedTone.NOTICE,
            title = "O simulado ENEM do 3º ano será no dia 24/10, das 7h30 às 12h30, no auditório.",
            time = "há 2 horas",
        ),
        FeedItem(
            id = "2",
            tag = "Atividade",
            tone = FeedTone.ACTIVITY,
            title = "Nova lista de exercícios de Matemática: funções do 2º grau. Entrega até sexta.",
            time = "há 5 horas",
        ),
        FeedItem(
            id = "3",
            tag = "Progresso",
            tone = FeedTone.ACHIEVEMENT,
            title = "Você concluiu 68% do módulo \"Genética e hereditariedade\".",
            time = "ontem",
        ),
        FeedItem(
            id = "4",
            tag = "Lembrete",
            tone = FeedTone.REMINDER,
            title = "Aula ao vivo de Física sobre as leis de Newton amanhã às 14h.",
            time = "ontem",
        ),
    )
}
