package com.digimon.demo.domain.todo

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("todos")
data class Todo(
        @Id
        var id: Long? = null,
        var content: String? = null,
        var done: Boolean = false,
        var createdAt: LocalDateTime = LocalDateTime.now(),
        var modifiedAt: LocalDateTime = createdAt
)
