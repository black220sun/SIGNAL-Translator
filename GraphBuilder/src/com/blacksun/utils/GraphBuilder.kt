package com.blacksun.utils

import java.io.File

class GraphBuilder {
    private val context = GraphBuilderContext()
    private val builder = StringBuilder()

    class GraphBuilderContext {
        var delimiter = "/"
        val ignoredPrefixes = ArrayList<String>()
        val ignoredSuffixes = ArrayList<String>()
        val ignoredPackages = ArrayList<String>()
        val ignoredUsages = ArrayList<String>()
        val interestingPackages = ArrayList<String>()
        val interestingUsages = ArrayList<String>()
        var text: String = ""
        var clusterize = false
        var simplifyNames = false

        fun isInterestingUser(path: String): Boolean {
            if (interestingPackages.isNotEmpty()) {
                return specified(interestingPackages, path)
            }
            return notSpecified(ignoredPackages, path)
        }

        fun isInterestingUsage(path: String): Boolean {
            if (interestingUsages.isNotEmpty()) {
                return specified(interestingUsages, path)
            }
            return notSpecified(ignoredUsages, path) && notSpecified(ignoredPackages, path)
        }

        private fun specified(where: Collection<String>, what: String): Boolean {
            return where.any { what.startsWith(it) }
        }

        private fun notSpecified(where: Collection<String>, what: String): Boolean {
            return where.none { what.startsWith(it) }
        }
    }

    fun withDelimiter(delimiter: String): GraphBuilder {
        context.delimiter = delimiter
        return this
    }

    fun withIgnoredPrefixes(vararg prefixes: String): GraphBuilder {
        context.ignoredPrefixes.clear()
        context.ignoredPrefixes.addAll(prefixes)
        return this
    }

    fun withIgnoredSuffixes(vararg suffixes: String): GraphBuilder {
        context.ignoredSuffixes.clear()
        context.ignoredSuffixes.addAll(suffixes)
        return this
    }

    fun withIgnoredPackages(vararg packages: String): GraphBuilder {
        context.ignoredPackages.clear()
        context.ignoredPackages.addAll(packages)
        return this
    }

    fun withIgnoredUsages(vararg usages: String): GraphBuilder {
        context.ignoredUsages.clear()
        context.ignoredUsages.addAll(usages)
        return this
    }

    fun withInterestingPackages(vararg packages: String): GraphBuilder {
        context.interestingPackages.clear()
        context.interestingPackages.addAll(packages)
        return this
    }

    fun withInterestingUsages(vararg usages: String): GraphBuilder {
        context.interestingUsages.clear()
        context.interestingUsages.addAll(usages)
        return this
    }

    fun withAdditionalFormatting(text: String): GraphBuilder {
        context.text = text + "\n"
        return this
    }

    fun withClustering(value: Boolean = true): GraphBuilder {
        context.clusterize = value
        return this
    }

    fun withSimpleNames(value: Boolean = true): GraphBuilder {
        context.simplifyNames = value
        return this
    }

    fun xmlToDot(filename: String) = xmlToDot(File(filename))

    fun xmlToDot(file: File): GraphBuilder {
        var filename = ""
        val nodesMap = HashMap<String, String>()
        var nodeCounter = 0
        val usedNodes = ArrayList<String>()
        val dependencies = ArrayList<String>()

        fun newNode(path: String) {
            if (! nodesMap.containsKey(path)) {
                nodesMap[path] = "node${nodeCounter++}"
                builder.append(nodesMap[path])
                        .append(" [label=\"")
                        .append(simplifyName(path.replace(context.delimiter, ".")))
                        .appendln("\", shape=box];")

            }
        }

        builder.appendln("digraph {")
        file.readLines().forEach {
            val line = it.trim()
            if (line.startsWith("<file ")) {
                val path = lineToPath(line)
                if (context.isInterestingUser(path)) {
                    filename = path
                    newNode(path)
                } else {
                    filename = ""
                }
            } else if (line.startsWith("<dependency ") && filename.isNotEmpty()) {
                val path = lineToPath(line)
                if (context.isInterestingUsage(path)) {
                    newNode(path)
                    if (! usedNodes.contains(path)) {
                        dependencies.add(nodesMap[path]!!)
                    }
                }
            } else if (line.startsWith("</file>") && filename.isNotEmpty()) {
                if (dependencies.isNotEmpty()) {
                    builder.append(nodesMap[filename])
                            .append(" -> { ")
                    dependencies.forEach { builder.append(it).append(" ") }
                    builder.appendln("};")
                    dependencies.clear()
                }
                usedNodes.add(filename)
                filename = ""
            }
        }
        if (context.clusterize) {
            clusterize(builder, nodesMap)
        }
        builder.append(context.text).appendln("}")
        return this
    }

    private fun simplifyName(name: String): String {
        if (! context.simplifyNames) {
            return name
        }
        return name.substring(name.lastIndexOf(".") + 1)
    }

    private fun clusterize(builder: StringBuilder, nodesMap: HashMap<String, String>) {
        val clusters = HashMap<String, ArrayList<String>>()
        nodesMap.forEach {
            val pkg = it.key.substring(0, it.key.indexOfLast { it == '/' })
            clusters[pkg] ?: clusters.put(pkg, ArrayList())
            clusters[pkg]?.add(it.value)
        }
        var cnt = 0
        clusters.forEach {
            if (it.value.size > 1) {
                builder.appendln("subgraph cluster${cnt++} { ")
//              builder.append("label=\"")
//                      .append(it.key.replace(context.delimiter, "."))
//                      .appendln("\t\";")
                it.value.forEach { builder.append(it).append("; ") }
                builder.appendln().appendln("}")
            }
        }
    }

    fun print(): GraphBuilder {
        println(builder)
        return this
    }

    fun toFile(filename: String) = toFile(File(filename))

    fun toFile(file: File): GraphBuilder {
        file.writeText(builder.toString())
        return this
    }

    private fun lineToPath(line: String): String {
        var path = line.substringAfter("path=\"")
                .substringBefore("\"")
        context.ignoredPrefixes.forEach {
            path = path.removePrefix(it)
        }
        context.ignoredSuffixes.forEach {
            path = path.removeSuffix(it)
        }
        return path
    }
}

fun main() {
    GraphBuilder()
            .withIgnoredPrefixes(
                    "\$PROJECT_DIR\$/src/main/kotlin/"
                    // , \$USER_HOME\$/Downloads/jdk1.8.0_181/jdk1.8.0_181/src.zip!/"
                    // , "\$MAVEN_REPOSITORY\$/org/jetbrains/kotlin/kotlin-stdlib/1.2.41/kotlin-stdlib-1.2.41.jar!/"
            )
            .withIgnoredSuffixes(".kt", ".java", ".class")
            .withIgnoredUsages(
                    "\$USER_HOME\$/Downloads/jdk1.8.0_181/jdk1.8.0_181/src.zip!/",
                    "\$MAVEN_REPOSITORY\$/org/jetbrains/kotlin/kotlin-stdlib/1.2.41/kotlin-stdlib-1.2.41.jar!/"
            )
            .withInterestingPackages("com/blacksun/utils/rule")
            .withInterestingUsages("com/blacksun/utils/rule", "com/blacksun/GrammarGen")
            //.withClustering()
            .withAdditionalFormatting("node6 -> node7 -> node1;")
            .withSimpleNames()
            .xmlToDot("graph.xml")
            .print()
            .toFile("graph.dot")
}