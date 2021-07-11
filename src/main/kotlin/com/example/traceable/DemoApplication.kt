package com.example.traceable

import com.github.javafaker.Faker
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.filter.CommonsRequestLoggingFilter

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

	@Bean
	fun requestLoggingFilter(): CommonsRequestLoggingFilter {
		val filter = CommonsRequestLoggingFilter()
		filter.setIncludeHeaders(true)
		filter.setIncludeQueryString(true)
		filter.setIncludeClientInfo(true)
		return filter
	}
}

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}


