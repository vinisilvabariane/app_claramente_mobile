package com.claramente.core.domain.contract

import com.claramente.core.model.classroom.Classroom

interface IRegenerateJoinCodeUseCase {
    suspend fun execute(classroomId: String): Result<Classroom>
}
