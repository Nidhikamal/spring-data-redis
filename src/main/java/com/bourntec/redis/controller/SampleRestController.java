package com.bourntec.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bourntec.redis.entity.Book;
import com.bourntec.redis.service.BookService;

@RestController
@RequestMapping("/redis")
public class SampleRestController {
	@Autowired
	BookService bookService;

	@GetMapping("")
	public ResponseEntity<Iterable<Book>> findAllSizeMainatinence() throws Exception {
		Iterable<Book> bookLst = bookService.findAll();
		return ResponseEntity.ok(bookLst);
	}

	@PostMapping("")
	public ResponseEntity<Book> saveSizeMaintainence(@RequestBody Book book) throws Exception {
		Book bookResp = bookService.save(book);
		return ResponseEntity.ok(bookResp);
	}

}
