package com.claramente.core.domain.contract

import com.claramente.core.model.classroom.Classroom

interface IListClassroomsUseCase {
    suspend fun execute(): Result<List<Classroom>>
}
