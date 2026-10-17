package com.claramente.architecture

import java.io.File
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class ArchitectureRulesTest {

    private val root: File = generateSequence(File(System.getProperty("user.dir")).absoluteFile) { it.parentFile }
        .first { File(it, "settings.gradle.kts").exists() }

    private val mainSources: List<File> = listOf("app", "core", "feature")
        .map { File(root, it) }
        .flatMap { dir -> dir.walkTopDown().filter { it.isFile && it.extension == "kt" }.toList() }
        .filter { it.invariantSeparatorsPath.contains("/src/main/kotlin/") }
        .filterNot { it.invariantSeparatorsPath.contains("/build/") }

    private val previewSources: List<File> = File(root, "app").walkTopDown()
        .filter { it.isFile && it.extension == "kt" }
        .filter { it.invariantSeparatorsPath.contains("/src/screenshotTest/kotlin/") }
        .filterNot { it.invariantSeparatorsPath.contains("/build/") }
        .toList()

    private val topLevel = Regex(
        "^(?:(?:public|internal|private|data|sealed|enum|abstract|open|inline|value|annotation|const|fun)\\s+)*" +
            "(class|interface|object|fun|val|var|typealias)\\s+(?:<[^>]+>\\s*)?(?:[A-Za-z_][\\w.]*\\.)?([A-Za-z_]\\w*)",
    )

    private val allowedFolders = setOf(
        "contract", "client", "store", "http", "dto", "mapper", "model", "policy", "helper",
        "view", "component", "state", "viewmodel", "controller", "service",
        "di", "navigation", "theme", "usecase", "error", "auth", "ar", "classroom",
    )

    private fun inFolder(file: File, folder: String) = file.invariantSeparatorsPath.contains("/$folder/")

    private val forbiddenInThinLayers = listOf(
        "import com.claramente.core.network.client.",
        "import com.claramente.core.network.contract.",
        "import com.claramente.core.network.http.ClaramenteHttp",
        "import com.claramente.core.data.store.",
        "import com.claramente.core.data.contract.",
    )

    private val forbiddenSuffixes = listOf("Manager", "Util", "Utils", "Helper", "Impl")

    private fun declarations(file: File): List<Pair<String, String>> =
        file.readLines()
            .filterNot { it.startsWith("package ") || it.startsWith("import ") || it.startsWith("@") }
            .mapNotNull { line -> topLevel.find(line)?.let { it.groupValues[1] to it.groupValues[2] } }

    private fun packageFolders(file: File): List<String> {
        val path = file.invariantSeparatorsPath.substringAfter("/src/main/kotlin/com/claramente/")
        val segments = path.split('/').dropLast(1)
        return when (segments.firstOrNull()) {
            "core", "feature" -> segments.drop(2)
            else -> segments
        }
    }

    @Test
    fun scanFindsSources() {
        assertTrue("nenhum arquivo .kt encontrado em ${root.path}", mainSources.size > 20)
    }

    @Test
    fun everyFileHasExactlyOneTopLevelDeclarationNamedLikeTheFile() {
        val violations = (mainSources + previewSources).mapNotNull { file ->
            val found = declarations(file)
            when {
                found.size != 1 -> "${file.relativeTo(root)}: ${found.size} declaracoes de topo ${found.map { it.second }}"
                found.single().second != file.nameWithoutExtension ->
                    "${file.relativeTo(root)}: declara ${found.single().second}, arquivo chama ${file.nameWithoutExtension}"
                else -> null
            }
        }
        if (violations.isNotEmpty()) fail(violations.joinToString("\n", "Um tipo por arquivo:\n"))
    }

    @Test
    fun foldersFollowTheRoleList() {
        val violations = mainSources.flatMap { file ->
            val folders = packageFolders(file)
            val inModule = file.invariantSeparatorsPath.let { it.contains("/core/") || it.contains("/feature/") }
            val loose = if (inModule && folders.isEmpty()) listOf("${file.relativeTo(root)}: fora de pasta de papel") else emptyList()
            loose + folders.filterNot { it in allowedFolders }.map { "${file.relativeTo(root)}: pasta '$it'" }
        }.distinct()
        if (violations.isNotEmpty()) fail(violations.joinToString("\n", "Pastas fora do padrao:\n"))
    }

    @Test
    fun noForbiddenTypeSuffixes() {
        val violations = mainSources.mapNotNull { file ->
            val name = file.nameWithoutExtension
            forbiddenSuffixes.firstOrNull { name.endsWith(it) }?.let { "${file.relativeTo(root)}: sufixo $it" }
        }
        if (violations.isNotEmpty()) fail(violations.joinToString("\n", "Sufixos proibidos:\n"))
    }

    @Test
    fun contractFoldersHoldOnlyPrefixedInterfaces() {
        val violations = mainSources.filter { inFolder(it, "contract") }.mapNotNull { file ->
            val declaration = declarations(file).singleOrNull()
            val name = file.nameWithoutExtension
            when {
                declaration?.first != "interface" -> "${file.relativeTo(root)}: contract/ so aceita interface"
                !Regex("^I[A-Z]").containsMatchIn(name) -> "${file.relativeTo(root)}: interface sem prefixo I"
                else -> null
            }
        }
        if (violations.isNotEmpty()) fail(violations.joinToString("\n", "Contratos fora do padrao:\n"))
    }

    @Test
    fun useCasesAreSingleOperations() {
        val publicFun = Regex("^\\s+(?:override\\s+)?(?:suspend\\s+)?(?:operator\\s+)?fun\\s+(\\w+)")
        val violations = mainSources.filter { inFolder(it, "usecase") }.mapNotNull { file ->
            val name = file.nameWithoutExtension
            val text = file.readText()
            val publicFuns = file.readLines()
                .filterNot { it.trimStart().startsWith("private ") }
                .mapNotNull { publicFun.find(it)?.groupValues?.get(1) }
            when {
                !name.endsWith("UseCase") -> "${file.relativeTo(root)}: usecase/ so aceita *UseCase"
                !Regex(":\\s*I$name\\b").containsMatchIn(text) -> "${file.relativeTo(root)}: nao implementa I$name"
                publicFuns != listOf("execute") -> "${file.relativeTo(root)}: metodos publicos $publicFuns (esperado so execute)"
                else -> null
            }
        }
        if (violations.isNotEmpty()) fail(violations.joinToString("\n", "Casos de uso fora do padrao:\n"))
    }

    @Test
    fun viewModelsAndControllersOnlyTalkToUseCases() {
        val violations = mainSources
            .filter { it.invariantSeparatorsPath.contains("/feature/") }
            .filter { inFolder(it, "viewmodel") || inFolder(it, "controller") }
            .flatMap { file ->
                file.readLines().filter { line -> forbiddenInThinLayers.any { line.startsWith(it) } }
                    .map { "${file.relativeTo(root)}: ${it.removePrefix("import ")}" }
            }
        if (violations.isNotEmpty()) fail(violations.joinToString("\n", "ViewModel/Controller acessando infraestrutura:\n"))
    }

    @Test
    fun serviceSuffixOnlyForAndroidServices() {
        val violations = mainSources
            .filter { it.nameWithoutExtension.endsWith("Service") && !inFolder(it, "service") }
            .map { "${it.relativeTo(root)}: *Service fora de service/" }
        if (violations.isNotEmpty()) fail(violations.joinToString("\n", "Sufixo Service:\n"))
    }

    @Test
    fun domainModelIsPureKotlin() {
        val violations = mainSources
            .filter { it.invariantSeparatorsPath.contains("/core/model/") }
            .filter { file -> file.readLines().any { it.startsWith("import org.json") || it.startsWith("import android") } }
            .map { it.relativeTo(root).path }
        if (violations.isNotEmpty()) fail(violations.joinToString("\n", "core:model com dependencia de plataforma:\n"))
    }
}
