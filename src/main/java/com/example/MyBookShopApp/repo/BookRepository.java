package com.example.MyBookShopApp.repo;

import com.example.MyBookShopApp.entity.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findBooksByAuthorId(Integer id);

    List<Book> findBooksByTitle(String title);

    //query with join because of Spring Data ignores annotation @Fetch(FetchMode.JOIN)
    //@Query(value = "SELECT * FROM shop.books b LEFT JOIN shop.authors a on b.author_id = a.id", nativeQuery = true)
    @Override
    Page<Book> findAll(Pageable pageable);

    List<Book> findBooksByAuthorFirstNameContaining(String authorFirstName);

    Page<Book> findBooksByTitleContaining(String bookTitle, Pageable nextPage);

    List<Book> findBooksByPriceOldBetween(Integer min, Integer max);

    List<Book> findBooksByPriceOldIs(Integer price);

    @Query("from Book where isBestseller=true")
    List<Book> getBestsellers();

    @Query(value = "SELECT * FROM shop.books WHERE discount = (SELECT MAX(discount) FROM shop.books)", nativeQuery = true)
    List<Book> getBooksWithMaxDiscount();

    Page<Book> findAllByIsBestsellerTrue(Pageable pageable);

    Page<Book> findAllByOrderByPubDateDesc(Pageable pageable);

}
