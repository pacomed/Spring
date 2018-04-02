package com.gcit.lms.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;

@RestController
public class AdminService extends BaseController{

	@Autowired
	AuthorDAO adao;

	@Autowired
	BookDAO bdao;
	
	@RequestMapping(value="updateAuthor", method=RequestMethod.POST, consumes="application/json" )
	@Transactional
	public void updateAuthor(@RequestBody Author author) throws SQLException {
		try {
			if (author.getAuthorId() != null && author.getAuthorName() != null) {
				adao.updateAuthor(author);
			} else if (author.getAuthorId() == null && author.getAuthorName() != null) {
				adao.createAuthor(author);
			} else {

				adao.deleteAuthor(author);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();// log your stacktrace
		}
	}

	@Transactional
	public void updateBook(Book book) throws SQLException {
		try {
			if (book.getBookId() != null && book.getTitle() != null) {
				bdao.updateBook(book);
				// call update book authors
				// caa update book genres
			} else if (book.getBookId() == null && book.getTitle() != null) {
				Integer bookId = bdao.createBookWithPK(book);
				for (Author a : book.getAuthors()) {
					// call adao.insert into tbl_book_authors (bookId,
					// a.getAuthroId()
				}
				// add to book authors
				// add to book genres
				// add to publisher
				// etc
			} else {
				bdao.deleteBook(book);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="readAuthors", method=RequestMethod.GET, produces="application/json")
	public List<Author> readAllAuthors(){
		List<Author> authors = new ArrayList<>();;
		try {
			authors = adao.readAuthors();
			for(Author a: authors){
				a.setBooks(bdao.readBooksByAuthorId(a));
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return authors;
	}
	
	@RequestMapping(value="readAuthorsByName/{searchString}", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Author> searchAuthorsByName(@PathVariable("searchString") String searchString){
		List<Author> authors = new ArrayList<>();;
		try {
			authors = adao.readAuthorsByName(searchString);
			for(Author a: authors){
				a.setBooks(bdao.readBooksByAuthorId(a));
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return authors;
	}

}
