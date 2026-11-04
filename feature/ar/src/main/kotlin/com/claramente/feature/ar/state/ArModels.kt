package com.claramente.feature.ar.state

import com.claramente.core.domain.policy.ModelQrPolicy

object ArModels {
    val SOLAR = ArModel(
        id = "solar",
        title = "Sistema Solar",
        subject = "Ciências",
        description = "O Sol com Vênus, Terra e Marte orbitando em velocidades diferentes.",
    )
    val AGUA = ArModel(
        id = "agua",
        title = "Molécula de água",
        subject = "Química",
        description = "H₂O com o ângulo real de 104,5° entre os hidrogênios.",
    )
    val SOLIDOS = ArModel(
        id = "solidos",
        title = "Sólidos geométricos",
        subject = "Matemática",
        description = "Cubo, esfera e pirâmide lado a lado para comparar as formas.",
    )

    val all: List<ArModel> = listOf(SOLAR, AGUA, SOLIDOS)
    val default: ArModel = SOLAR

    fun resolveFromQr(data: String, models: List<ArModel> = all): ArModel? {
        val id = ModelQrPolicy.idFrom(data)
        return models.firstOrNull { it.id.lowercase() == id }
    }
}
