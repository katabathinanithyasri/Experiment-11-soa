package com.klu.springmvc;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.klu.springmvc.model.Book;
import com.klu.springmvc.service.BookService;
import com.klu.springmvc.service.JwtService;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService service;
    private final JwtService jwtService;

    public BookController(BookService service, JwtService jwtService) {
        this.service = service;
        this.jwtService = jwtService;
    }

    // Generate JWT Token
    @PostMapping("/token")
    public String generateToken(@RequestBody Map<String, String> data) {

        String username = data.get("username");
        String role = data.get("role");

        return jwtService.generateToken(username, role);
    }

    // Get all books
    @GetMapping
    public List<Book> getAllBooks(
            @RequestHeader("Authorization") String token) {

        token = token.substring(7);

        return service.getAllBooks(token);
    }

    // Add book
    @PostMapping
    public Book addBook(
            @RequestHeader("Authorization") String token,
            @RequestBody Book book) {

        token = token.substring(7);

        return service.addBook(book, token);
    }

    // Update book
    @PutMapping("/{id}")
    public Book updateBook(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token,
            @RequestBody Book book) {

        token = token.substring(7);

        return service.updateBook(id, book, token);
    }

    // Delete book
    @DeleteMapping("/{id}")
    public String deleteBook(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        token = token.substring(7);

        service.deleteBook(id, token);

        return "Book deleted successfully";
    }
}