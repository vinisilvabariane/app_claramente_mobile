package com.claramente.core.domain.contract

import com.claramente.core.model.classroom.ClassroomStudent

interface IListStudentsUseCase {
    suspend fun execute(classroomId: String): Result<List<ClassroomStudent>>
}
