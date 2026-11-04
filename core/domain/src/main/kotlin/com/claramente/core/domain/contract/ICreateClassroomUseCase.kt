package com.claramente.core.domain.contract

import com.claramente.core.model.classroom.Classroom

interface ICreateClassroomUseCase {
    suspend fun execute(name: String, subject: String?): Result<Classroom>
}
