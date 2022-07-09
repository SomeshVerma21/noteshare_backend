package com.gamest.notebook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.RequestMapping

@ComponentScan("com.gamest.notebook.repo","com.gamest.notebook.user.service","com.gamest.notebook.user.controllers","com.gamest.notebook.staticPages.WebPageController"
	,"com.gamest.notebook.notes.controllers","com.gamest.notebook.notes.services","com.gamest.notebook.notes.storageService")
@SpringBootApplication
class NotebookApplication

fun main(args: Array<String>) {
	runApplication<NotebookApplication>(*args)
}

@RequestMapping("/")
fun home() = "NoteShare"

