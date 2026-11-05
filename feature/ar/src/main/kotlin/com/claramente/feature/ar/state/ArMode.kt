package com.claramente.feature.ar.state

enum class ArMode(val title: String, val caption: String, val hint: String, val usesCamera: Boolean = true) {
    MARKER("Marcador", "Folha impressa", "Aponte para a folha impressa · arraste para girar o modelo"),
    CUBE("Cubo", "Merge Cube", "Gire o cubo na mão ou arraste para girar o modelo"),
    QR("QR code", "Sem marcador", "Aponte para um QR code do material · arraste para girar"),
    VIEWER("Ver em 3D", "Sem câmera", "Arraste para girar · junte dois dedos para aproximar", usesCamera = false);

    companion object {
        val camera: List<ArMode> = entries.filter { it.usesCamera }
    }
}
