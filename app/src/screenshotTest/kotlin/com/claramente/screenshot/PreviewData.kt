package com.claramente.screenshot

import com.claramente.core.model.classroom.Classroom
import com.claramente.core.model.classroom.ClassroomStudent
import com.claramente.core.model.classroom.Model3D

internal object PreviewData {
    val classroom = Classroom("c1", "1º ano A", "Biologia", "K7M2QX", "Carla Mendes", 2, 1)
    val students = listOf(
        ClassroomStudent("s1", "Ana Souza", "ana@escola.br"),
        ClassroomStudent("s2", "Bruno Lima", "bruno@escola.br"),
    )
    val cell = Model3D("m1", "Célula animal", "Biologia", "Observe o núcleo e as mitocôndrias.", 2_480_000, "abc", "Carla Mendes", listOf("c1"))
    val dna = Model3D("m2", "Dupla hélice do DNA", "Biologia", null, 860_000, "def", "Carla Mendes", emptyList())
}
