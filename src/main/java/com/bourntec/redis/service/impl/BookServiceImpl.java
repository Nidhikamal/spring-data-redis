package com.bourntec.redis.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.bourntec.redis.entity.Book;
import com.bourntec.redis.queue.MessagePublisher;
import com.bourntec.redis.repository.BookRepository;
import com.bourntec.redis.service.BookService;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	MessagePublisher redisPublisher;

	public Book save(Book book) {
		Book resp = new Book();

		try {
			resp = bookRepository.save(book);

			//redisPublisher.publish("firstQ", "set name ww");

			redisPublisher.publish("receiveCountMessage", "set name srijini");
			// redisPublisher.publish("receiveNotificationMessage", "set name tt");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;

	}

	public void delete(Book book) {
		bookRepository.delete(book);
	}

	/*
	 * public Book findOne(String id) { return bookRepository.findOne(id); }
	 */

	public Iterable<Book> findAll() {
		return bookRepository.findAll();
	}

	public Page<Book> findByAuthor(String author, PageRequest pageRequest) {
		return bookRepository.findByAuthor(author, pageRequest);
	}

	public List<Book> findByTitle(String title) {
		return bookRepository.findByTitle(title);
	}
}
