package com.claramente.core.domain.contract

import com.claramente.core.model.classroom.Model3D

interface IListClassroomModelsUseCase {
    suspend fun execute(classroomId: String): Result<List<Model3D>>
}
