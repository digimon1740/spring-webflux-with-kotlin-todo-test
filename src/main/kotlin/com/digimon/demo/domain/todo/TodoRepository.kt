package com.digimon.demo.domain.todo

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface TodoRepository : ReactiveCrudRepository<Todo, Long>

