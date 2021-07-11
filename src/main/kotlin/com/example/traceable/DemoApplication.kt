package com.example.traceable

import com.github.javafaker.Faker
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class DemoApplication {

	@Bean
	fun loadBooks(bookShelf: BookShelf) = ApplicationRunner {
		val bookGenerator = Faker().book()

		bookShelf.deleteAll()

		for (i in 1..100) {
			bookShelf.save(Book(
				title = bookGenerator.title(),
				author = bookGenerator.author(),
				genre = Genre.normalised(bookGenerator.genre()),
				publisher = bookGenerator.publisher()
			))
		}
	}
}

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}


