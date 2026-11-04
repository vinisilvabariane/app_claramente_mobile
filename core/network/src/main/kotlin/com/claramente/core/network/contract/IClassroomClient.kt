package com.claramente.core.network.contract

import com.claramente.core.model.classroom.Classroom
import com.claramente.core.model.classroom.ClassroomStudent
import com.claramente.core.model.classroom.Model3D
import com.claramente.core.network.http.AuthSession

interface IClassroomClient {
    fun list(session: AuthSession): List<Classroom>

    fun create(session: AuthSession, name: String, subject: String?): Classroom

    fun delete(session: AuthSession, classroomId: String)

    fun regenerateCode(session: AuthSession, classroomId: String): Classroom

    fun join(session: AuthSession, code: String): Classroom

    fun leave(session: AuthSession, classroomId: String)

    fun students(session: AuthSession, classroomId: String): List<ClassroomStudent>

    fun removeStudent(session: AuthSession, classroomId: String, studentId: String)

    fun models(session: AuthSession, classroomId: String): List<Model3D>

    fun publish(session: AuthSession, classroomId: String, modelId: String)

    fun unpublish(session: AuthSession, classroomId: String, modelId: String)
}
