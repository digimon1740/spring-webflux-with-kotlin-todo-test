package com.digimon.demo

import com.digimon.demo.domain.todo.Todo
import com.digimon.demo.domain.todo.TodoRepository
import com.digimon.demo.handler.TodoHandler
import com.digimon.demo.router.TodoRouter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@WebFluxTest
@RunWith(SpringRunner::class)
class TodoApplicationTests {

    @MockBean
    lateinit var repo: TodoRepository

    lateinit var webClient: WebTestClient

    lateinit var todo1: Todo

    lateinit var todo2: Todo

    @Before
    fun setUp() {
        todo1 = Todo(id = 1L, content = "I have to finish my work by tomorrow")
        todo2 = Todo(id = 2L, content = "Get rid of security flaws in my app")

        val routerFunction = TodoRouter(TodoHandler(repo)).routerFunction()
        webClient = WebTestClient.bindToRouterFunction(routerFunction).build()
    }

    @Test
    @Throws(Exception::class)
    fun `test should return a list of todo`() {
        given(repo.findAll()).willReturn(Flux.just(todo1, todo2))

        val responseBody: List<Todo>? = webClient.get().uri("/todos").accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Todo::class.java)
            .hasSize(2)
            .returnResult().responseBody

        assertThat(responseBody?.get(1)?.id).isEqualTo(2L)
        assertThat(responseBody?.get(1)?.content).isEqualTo("Get rid of security flaws in my app")
    }

    @Test
    @Throws(Exception::class)
    fun `test should return an item of todo by id`() {
        given(repo.findById(1L)).willReturn(Mono.just(todo1))

        val responseBody: Todo? = webClient.get().uri("/todos/{id}", 1L).accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(Todo::class.java)
            .returnResult().responseBody

        assertThat(responseBody?.content).isEqualTo("I have to finish my work by tomorrow")
    }

}
