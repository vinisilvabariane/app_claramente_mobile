package com.claramente.core.network.mapper

import com.claramente.core.model.classroom.Classroom
import com.claramente.core.model.classroom.ClassroomStudent
import com.claramente.core.model.classroom.Model3D
import org.json.JSONArray
import org.json.JSONObject

internal object ClassroomJsonMapper {
    fun classroom(json: JSONObject): Classroom = Classroom(
        id = json.optString("id"),
        name = json.optString("name"),
        subject = text(json, "subject"),
        joinCode = text(json, "joinCode"),
        teacherName = json.optString("teacherName"),
        studentCount = json.optInt("studentCount"),
        modelCount = json.optInt("modelCount"),
    )

    fun classrooms(raw: String): List<Classroom> = objects(raw).map(::classroom)

    fun students(raw: String): List<ClassroomStudent> = objects(raw).map { json ->
        ClassroomStudent(id = json.optString("id"), name = json.optString("name"), email = json.optString("email"))
    }

    fun model(json: JSONObject): Model3D = Model3D(
        id = json.optString("id"),
        title = json.optString("title"),
        subject = text(json, "subject"),
        description = text(json, "description"),
        sizeBytes = json.optLong("sizeBytes"),
        sha256 = json.optString("sha256"),
        ownerName = json.optString("ownerName"),
        classroomIds = json.optJSONArray("classroomIds")?.let { ids -> (0 until ids.length()).map(ids::optString) } ?: emptyList(),
    )

    fun models(raw: String): List<Model3D> = objects(raw).map(::model)

    private fun objects(raw: String): List<JSONObject> {
        val array = JSONArray(raw.ifBlank { "[]" })
        return (0 until array.length()).map(array::getJSONObject)
    }

    private fun text(json: JSONObject, key: String): String? =
        json.takeIf { it.has(key) && !it.isNull(key) }?.optString(key)?.trim()?.takeIf { it.isNotEmpty() }
}
