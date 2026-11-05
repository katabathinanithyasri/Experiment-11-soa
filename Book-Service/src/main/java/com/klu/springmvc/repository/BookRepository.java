package com.klu.springmvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klu.springmvc.model.Book;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

}