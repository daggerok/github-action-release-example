package daggerok

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.reactive.result.view.Rendering

@Controller
class MyResource {

    @GetMapping("/")
    fun index() = Rendering.view("index").modelAttribute("message", "hello!").build()
}

@SpringBootApplication
class MyMainApplication

fun main(args: Array<String>) {
    runApplication<MyMainApplication>(*args)
}
