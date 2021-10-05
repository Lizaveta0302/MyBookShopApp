package com.example.MyBookShopApp.repo;

import com.example.MyBookShopApp.entity.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findBooksByAuthorId(Integer id);

    List<Book> findBooksByTitle(String title);

    //query with join because of Spring Data ignores annotation @Fetch(FetchMode.JOIN)
    @Query(value = "SELECT b FROM Book b LEFT JOIN FETCH b.author")
    @Override
    List<Book> findAll();

    List<Book> findBooksByAuthorFirstNameContaining(String authorFirstName);

    List<Book> findBooksByTitleContaining(String bookTitle);

    List<Book> findBooksByPriceOldBetween(Integer min, Integer max);

    List<Book> findBooksByPriceOldIs(Integer price);

    @Query("from Book where isBestseller=1")
    List<Book> getBestsellers();

    @Query(value = "SELECT * FROM shop.books WHERE discount = (SELECT MAX(discount) FROM shop.books)", nativeQuery = true)
    List<Book> getBooksWithMaxDiscount();
}
