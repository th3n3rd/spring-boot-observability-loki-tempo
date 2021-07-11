package com.example.traceable

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class LibraryController(private val library: Library) {

    @GetMapping("/genres")
    fun listGenre(): GenreResponse {
        return GenreResponse(library.listGenres())
    }

    @GetMapping("/genres/{genreSlug}/books")
    fun listBooksForGenre(@PathVariable genreSlug: String): BooksResponse {
        return BooksResponse(
            library.listBooksByGenre(genreSlug).map {
                BookResponse(
                    it.id,
                    it.title
                )
            }
        )
    }

    @GetMapping("/books/{bookId}")
    fun bookDetails(@PathVariable bookId: UUID): BookDetailsResponse {
        val book = library.bookDetails(bookId)
        return BookDetailsResponse(
            book.id,
            book.title,
            book.author,
            book.genre.description,
            book.publisher
        )
    }

    data class GenreResponse(
        val genres: List<Genre>
    )

    data class BooksResponse(
        val books: List<BookResponse>
    )

    data class BookResponse(
        val id: UUID,
        val title: String,
    )

    data class BookDetailsResponse(
        val id: UUID,
        val title: String,
        val author: String,
        val genre: String,
        val publisher: String
    )
}

@Service
class Library(private val bookShelf: BookShelf, private val mongoTemplate: MongoTemplate) {

    fun listGenres(): List<Genre> = mongoTemplate
        .query(Book::class.java)
        .distinct("genre")
        .`as`(Genre::class.java)
        .all()

    fun listBooksByGenre(genreSlug: String) = bookShelf.findAllByGenreSlug(genreSlug)

    fun bookDetails(bookId: UUID) = bookShelf.findByIdOrNull(bookId) ?: throw BookNotFoundException()
}

@Repository
interface BookShelf : MongoRepository<Book, UUID> {
    fun findAllByGenreSlug(genreSlug: String): List<Book>
}

data class Book(
    @Id
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val author: String,
    val genre: Genre,
    val publisher: String
)

data class Genre(val slug: String, val description: String) {
    companion object {
        fun normalised(description: String): Genre {
            return Genre(
                description
                    .toLowerCase()
                    .replace("/", " or ")
                    .replace(" ", "-"),
                description
            )
        }
    }
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class BookNotFoundException : RuntimeException()
