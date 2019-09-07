package com.digimon.demo

import com.digimon.demo.domain.todo.Todo
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest
@RunWith(SpringRunner::class)
class TodoApplicationTests {

    @Autowired
    lateinit var webClient: WebTestClient

    @Test
    @Throws(Exception::class)
    fun `test should return a list of todo`() {
        webClient.get().uri("/todos").accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Todo::class.java)
            .hasSize(3)
    }

}
