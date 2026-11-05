package com.klu.springmvc.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.klu.springmvc.model.Book;
import com.klu.springmvc.repository.BookRepository;

@Service
public class BookService {

    private final BookRepository repository;
    private final JwtService jwtService;

    public BookService(BookRepository repository, JwtService jwtService) {
        this.repository = repository;
        this.jwtService = jwtService;
    }

    // GET - ADMIN and USER
    public List<Book> getAllBooks(String token) {

        if (!jwtService.isTokenValid(token)) {
            throw new RuntimeException("Invalid JWT token");
        }

        return repository.findAll();
    }

    // ADD - ADMIN only
    public Book addBook(Book book, String token) {

        checkAdmin(token);

        return repository.save(book);
    }

    // UPDATE - ADMIN only
    public Book updateBook(Long id, Book book, String token) {

        checkAdmin(token);

        Book existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        existing.setTitle(book.getTitle());
        existing.setAuthor(book.getAuthor());
        existing.setIsbn(book.getIsbn());

        return repository.save(existing);
    }

    // DELETE - ADMIN only
    public void deleteBook(Long id, String token) {

        checkAdmin(token);

        if (!repository.existsById(id)) {
            throw new RuntimeException("Book not found");
        }

        repository.deleteById(id);
    }

    // Check ADMIN
    private void checkAdmin(String token) {

        if (!jwtService.isTokenValid(token)) {
            throw new RuntimeException("Invalid JWT token");
        }

        String role = jwtService.extractRole(token);

        if (!role.equals("ADMIN")) {
            throw new RuntimeException("Access denied. ADMIN only.");
        }
    }
}