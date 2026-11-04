package com.claramente.core.domain.error

object ClassroomErrors {
    const val MOCK_MODE = "O modo teste não acessa o servidor. Entre com uma conta real."
    const val EMPTY_NAME = "Informe o nome da turma."
    const val INVALID_CODE = "O código da turma tem 6 letras ou números."
    const val EMPTY_TITLE = "Informe o título do modelo."
    const val MISSING_FILE = "Escolha o arquivo do modelo."
    const val FILE_TOO_LARGE = "O modelo passa do limite de 25 MB."
    const val NOT_GLB = "Escolha um arquivo .glb (glTF 2.0 binário)."
    const val FILE_UNREADABLE = "Não foi possível ler o arquivo escolhido."
}
