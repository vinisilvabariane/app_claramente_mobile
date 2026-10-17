package com.claramente.core.domain.usecase

import com.claramente.core.model.classroom.Classroom
import com.claramente.core.model.classroom.ClassroomStudent
import com.claramente.core.model.classroom.Model3D
import com.claramente.core.network.contract.IClassroomClient
import com.claramente.core.network.http.AuthSession

internal class FakeClassroomClient : IClassroomClient {
    val calls = mutableListOf<String>()
    private val classroom = Classroom("c1", "1º ano A", "Biologia", null, "Carla", 1, 0)

    override fun list(session: AuthSession): List<Classroom> = listOf(classroom).also { calls += "list" }

    override fun create(session: AuthSession, name: String, subject: String?): Classroom =
        classroom.copy(name = name, subject = subject).also { calls += "create:$name:$subject" }

    override fun delete(session: AuthSession, classroomId: String) {
        calls += "delete:$classroomId"
    }

    override fun regenerateCode(session: AuthSession, classroomId: String): Classroom = classroom.also { calls += "code:$classroomId" }

    override fun join(session: AuthSession, code: String): Classroom = classroom.also { calls += "join:$code" }

    override fun leave(session: AuthSession, classroomId: String) {
        calls += "leave:$classroomId"
    }

    override fun students(session: AuthSession, classroomId: String): List<ClassroomStudent> = emptyList()

    override fun removeStudent(session: AuthSession, classroomId: String, studentId: String) {
        calls += "remove:$classroomId:$studentId"
    }

    override fun models(session: AuthSession, classroomId: String): List<Model3D> = emptyList()

    override fun publish(session: AuthSession, classroomId: String, modelId: String) {
        calls += "publish:$classroomId:$modelId"
    }

    override fun unpublish(session: AuthSession, classroomId: String, modelId: String) {
        calls += "unpublish:$classroomId:$modelId"
    }
}
