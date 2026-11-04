package com.claramente.core.domain.contract

import com.claramente.core.model.classroom.Classroom

interface IJoinClassroomUseCase {
    suspend fun execute(code: String): Result<Classroom>
}
