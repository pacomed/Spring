/**
 * 
 */
package com.gcit.lms.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;

/**
 * @book ppradhan
 *
 */
@Component
public class BookDAO extends BaseDAO<Book> implements ResultSetExtractor<List<Book>>{
	
	public void createBook(Book book) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("insert into tbl_book (title) values (?)", new Object[]{book.getTitle()});
	}
	
	public Integer createBookWithPK(Book book) throws ClassNotFoundException, SQLException {
		  String insertSql = "insert into tbl_book (title) values (?)";
		  GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		  String id_column = "ID";
		  // the update method takes an implementation of PreparedStatementCreator which could be a lambda
		  jdbcTemplate.update(con -> {
		    PreparedStatement ps = con.prepareStatement(insertSql, new String[]{id_column});
		    ps.setString(1, book.getTitle());
		    return ps;
		  }
		  , keyHolder);
		  
		  BigDecimal id = (BigDecimal) keyHolder.getKeys().get(id_column);
		  return id.intValue();
	}

	public void updateBook(Book book) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("update tbl_book set title =? where bookId = ?", new Object[]{book.getTitle(), book.getBookId()});
	}

	public void deleteBook(Book book) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("delete from tbl_book where bookId = ?", new Object[]{book.getBookId()});
	}
	
	public List<Book> readBooks() throws ClassNotFoundException, SQLException {
		return jdbcTemplate.query("select * from tbl_book", this);
	}
	
	public List<Book> readBooksByTitle(String bookTitle) throws ClassNotFoundException, SQLException {
		bookTitle = "%"+bookTitle+"%";
		return jdbcTemplate.query("select * from tbl_book WHERE title like ?", new Object[]{bookTitle}, this);
	}
	
	public List<Book> readBooksByAuthorId(Author author) throws ClassNotFoundException, SQLException {
		return jdbcTemplate.query("select * from tbl_book where bookId IN (Select bookId from tbl_book_authors where authorId =?)", new Object[]{ author.getAuthorId()}, this); 
	}
	
	public Book getBookByPk(Book book) throws ClassNotFoundException, SQLException {
		List<Book> books = jdbcTemplate.query("select * from tbl_book WHERE bookId = ?", new Object[]{book.getBookId()}, this);
		if(books!=null){
			return books.get(0);
		}
		return null;
	}

	public List<Book> extractData(ResultSet rs) throws SQLException {
		List<Book> books = new ArrayList<>();
		while(rs.next()){
			Book book = new Book();
			book.setBookId(rs.getInt("bookId"));
			book.setTitle(rs.getString("title"));
			books.add(book);
		}
		return books;
	}
}
