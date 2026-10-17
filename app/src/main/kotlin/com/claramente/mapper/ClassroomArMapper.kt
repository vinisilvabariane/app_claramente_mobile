package com.claramente.mapper

import com.claramente.core.model.classroom.Model3D
import com.claramente.feature.ar.state.ArModel

object ClassroomArMapper {
    fun toArModel(model: Model3D, fileUri: String): ArModel = ArModel(
        id = model.id,
        title = model.title,
        subject = model.subject ?: "Modelo da turma",
        description = model.description ?: "Enviado por ${model.ownerName}",
        fileUri = fileUri,
    )
}
