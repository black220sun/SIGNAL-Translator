package com.blacksun.gui

import com.blacksun.gui.util.LImage
import com.blacksun.utils.node.Node
import javax.swing.JScrollPane
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeCellRenderer
import javax.swing.tree.DefaultTreeModel

class TreePanel(node: Node): JScrollPane() {
    private val tree = JTree(NodeTreeModel(node))

    init {
        expandAllNodes(tree, 0, tree.rowCount)
    }

    private fun expandAllNodes(tree: JTree, startingIndex: Int, rowCount: Int) {
        for (i in startingIndex until rowCount) {
            tree.expandRow(i)
        }
        if (tree.rowCount != rowCount) {
            expandAllNodes(tree, rowCount, tree.rowCount)
        }
    }

    class NodeTreeModel(node: Node): DefaultTreeModel(DefaultMutableTreeNode(node)) {
        override fun isLeaf(node: Any): Boolean {
            val parent = (node as DefaultMutableTreeNode).userObject as Node
            return parent.children() == 0
        }

        override fun getChild(node: Any, index: Int): Any {
            val parent = (node as DefaultMutableTreeNode).userObject as Node
            return DefaultMutableTreeNode(parent[index])
        }

        override fun getChildCount(node: Any): Int {
            val parent = (node as DefaultMutableTreeNode).userObject as Node
            return parent.children()
        }
    }

    init {
        viewport.view = tree
        tree.cellRenderer = Renderer()
    }

    class Renderer : DefaultTreeCellRenderer() {
        init {
            leafIcon = LImage("leaf.png")
            openIcon = leafIcon
            closedIcon = leafIcon
        }
    }
}