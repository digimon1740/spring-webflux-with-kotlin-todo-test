package com.digimon.demo

import com.digimon.demo.domain.todo.Todo
import com.digimon.demo.domain.todo.TodoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoApplicationTests {

    @Autowired
    lateinit var webClient: WebTestClient

    @MockBean
    lateinit var repo: TodoRepository

    lateinit var todo: Todo

    @Before
    fun setUp() {
        todo = Todo(id = 1L, content = "I have to finish my work by tomorrow")
    }

    @Test
    @Throws(Exception::class)
    fun `test should return a list of todo`() {
        given(repo.findAll()).willReturn(Flux.just(todo))

        webClient.get().uri("/todos").accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Todo::class.java)
            .hasSize(1)
    }

    @Test
    @Throws(Exception::class)
    fun `test should return a todo by id`() {
        given(repo.findById(1L)).willReturn(Mono.just(todo))

        val responseBody: Todo? = webClient.get().uri("/todos/{id}", 1L).accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(Todo::class.java)
            .returnResult().responseBody

        assertThat(responseBody?.content).isEqualTo("I have to finish my work by tomorrow")
    }

}
