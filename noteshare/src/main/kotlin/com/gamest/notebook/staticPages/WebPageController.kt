package com.gamest.notebook.staticPages

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class WebPageController {
    @GetMapping("/home")
    fun home(): String{
        return "Home NoteShare"
    }
}