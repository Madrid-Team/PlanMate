package presentation.utils

import presentation.components.OutputPrinter

fun OutputPrinter.printMenuItems(items: List<String>) {
    items.forEach { this.printMessage(it) }
}