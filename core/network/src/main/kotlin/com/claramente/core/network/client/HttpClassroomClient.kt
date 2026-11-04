package com.claramente.core.network.client

import com.claramente.core.model.classroom.Classroom
import com.claramente.core.model.classroom.ClassroomStudent
import com.claramente.core.model.classroom.Model3D
import com.claramente.core.network.contract.IClassroomClient
import com.claramente.core.network.http.AuthSession
import com.claramente.core.network.http.ClaramenteHttp
import com.claramente.core.network.mapper.ClassroomJsonMapper
import org.json.JSONObject

class HttpClassroomClient(private val http: ClaramenteHttp) : IClassroomClient {
    override fun list(session: AuthSession): List<Classroom> =
        ClassroomJsonMapper.classrooms(call(session, "GET", ROOT))

    override fun create(session: AuthSession, name: String, subject: String?): Classroom {
        val body = JSONObject().put("name", name).put("subject", subject ?: JSONObject.NULL)
        return ClassroomJsonMapper.classroom(JSONObject(call(session, "POST", ROOT, body)))
    }

    override fun delete(session: AuthSession, classroomId: String) {
        call(session, "DELETE", "$ROOT/$classroomId")
    }

    override fun regenerateCode(session: AuthSession, classroomId: String): Classroom =
        ClassroomJsonMapper.classroom(JSONObject(call(session, "POST", "$ROOT/$classroomId/join-code")))

    override fun join(session: AuthSession, code: String): Classroom =
        ClassroomJsonMapper.classroom(JSONObject(call(session, "POST", "$ROOT/join", JSONObject().put("code", code))))

    override fun leave(session: AuthSession, classroomId: String) {
        call(session, "DELETE", "$ROOT/$classroomId/enrollment")
    }

    override fun students(session: AuthSession, classroomId: String): List<ClassroomStudent> =
        ClassroomJsonMapper.students(call(session, "GET", "$ROOT/$classroomId/students"))

    override fun removeStudent(session: AuthSession, classroomId: String, studentId: String) {
        call(session, "DELETE", "$ROOT/$classroomId/students/$studentId")
    }

    override fun models(session: AuthSession, classroomId: String): List<Model3D> =
        ClassroomJsonMapper.models(call(session, "GET", "$ROOT/$classroomId/models"))

    override fun publish(session: AuthSession, classroomId: String, modelId: String) {
        call(session, "PUT", "$ROOT/$classroomId/models/$modelId")
    }

    override fun unpublish(session: AuthSession, classroomId: String, modelId: String) {
        call(session, "DELETE", "$ROOT/$classroomId/models/$modelId")
    }

    private fun call(session: AuthSession, method: String, path: String, body: JSONObject? = null): String =
        http.authenticated(session) { token -> http.send(method, path, body, token) }

    private companion object {
        const val ROOT = "/classrooms"
    }
}
