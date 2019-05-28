package com.blacksun.utils

import java.io.File

class GraphBuilder {
    private val context = GraphBuilderContext()
    private val builder = StringBuilder()

    class GraphBuilderContext {
        var delimiter = "/"
        var ignoredPrefixes = emptyList<String>()
        var ignoredSuffixes = emptyList<String>()
        var ignoredPackages = emptyList<String>()
        var ignoredUsages = emptyList<String>()
        var text: String = ""
        var clusterize = false
    }

    fun withDelimiter(delimiter: String): GraphBuilder {
        context.delimiter = delimiter
        return this
    }

    fun withIgnoredPrefixes(vararg prefixes: String): GraphBuilder {
        context.ignoredPrefixes = prefixes.asList()
        return this
    }

    fun withIgnoredSuffixes(vararg suffixes: String): GraphBuilder {
        context.ignoredSuffixes = suffixes.asList()
        return this
    }

    fun withIgnoredPackages(vararg packages: String): GraphBuilder {
        context.ignoredPackages = packages.asList()
        return this
    }

    fun withIgnoredUsages(vararg usages: String): GraphBuilder {
        context.ignoredUsages = usages.asList()
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

    fun xmlToDot(filename: String) = xmlToDot(File(filename))

    fun xmlToDot(file: File): GraphBuilder {
        var filename = ""
        val nodesMap = HashMap<String, String>()
        var nodeCounter = 0
        var usedNodes = ArrayList<String>()
        val dependencies = ArrayList<String>()

        fun newNode(path: String) {
            if (! nodesMap.containsKey(path)) {
                nodesMap[path] = "node${nodeCounter++}"
                builder.append(nodesMap[path])
                        .append(" [label=\"")
                        .append(path.replace(context.delimiter, "."))
                        .appendln("\", shape=box];")

            }
        }

        builder.appendln("digraph {")
        file.readLines().forEach {
            val line = it.trim()
            if (line.startsWith("<file ")) {
                val path = lineToPath(line)
                if (context.ignoredPackages.none { path.startsWith(it) }) {
                    filename = path
                    newNode(path)
                } else {
                    filename = ""
                }
            } else if (line.startsWith("<dependency ") && filename.isNotEmpty()) {
                val path = lineToPath(line)
                if (context.ignoredUsages.none { path.startsWith(it) }) {
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
                    "\$MAVEN_REPOSITORY\$/org/jetbrains/kotlin/kotlin-stdlib/1.2.41/kotlin-stdlib-1.2.41.jar!/",
                    "com/blacksun/Logger", "com/blacksun/utils/node",
                    "com/blacksun/utils/rule", "com/blacksun/utils/Token"
            )
            .withIgnoredPackages(
                    "com/blacksun/gui", "com/blacksun/optimizer",
                    "com/blacksun/Main", "com/blacksun/settings",
                    "com/blacksun/utils/node", "com/blacksun/Logger"
            )
            .withAdditionalFormatting("node1 -> node9 -> node8 -> node7;\n" +
                    "node7 -> { node2 node3 node4 node5 node6 };\n" +
                    "node3 -> node10;\n" +
                    "node6 -> node10;")
            .withClustering()
            .xmlToDot("graph.xml")
            .print()
            .toFile("graph.dot")
}