/**
 * 
 */
package com.gcit.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.gcit.lms.entity.Author;

/**
 * @author ppradhan
 *
 */
@Component
public class AuthorDAO extends BaseDAO<Author> implements ResultSetExtractor<List<Author>>{
	
	public void createAuthor(Author author) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("insert into tbl_author (authorName) values (?)", new Object[]{author.getAuthorName()});
	}

	public void updateAuthor(Author author) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("update tbl_author set authorName =? where authorId = ?", new Object[]{author.getAuthorName(), author.getAuthorId()});
	}

	public void deleteAuthor(Author author) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("delete from tbl_author where authorId = ?", new Object[]{author.getAuthorId()});
	}
	
	public List<Author> readAuthors() throws ClassNotFoundException, SQLException {
		return jdbcTemplate.query("select * from tbl_author", this);
	}
	
	public List<Author> readAuthorsByName(String searchString) throws ClassNotFoundException, SQLException {
		searchString = "%"+searchString+"%";
		return jdbcTemplate.query("select * from tbl_author where authorName like ?", new Object[]{searchString}, this);
	}

	@Override
	public List<Author> extractData(ResultSet rs) throws SQLException {
		List<Author> authors = new ArrayList<>();
		while(rs.next()){
			Author author = new Author();
			author.setAuthorId(rs.getInt("authorId"));
			author.setAuthorName(rs.getString("authorName"));
			authors.add(author);
		}
		return authors;
	}
}
