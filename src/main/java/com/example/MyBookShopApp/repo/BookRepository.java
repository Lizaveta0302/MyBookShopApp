package com.example.MyBookShopApp.repo;

import com.example.MyBookShopApp.entity.Tag;
import com.example.MyBookShopApp.entity.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findBooksByAuthorId(Integer id);

    Page<Book> findBooksByAuthorId(Pageable nextPage, Integer id);

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

    Page<Book> findAllByPubDateBetweenOrderByPubDateDesc(Pageable pageable, Date from, Date to);

    @Query(value = "select *, (b.number_of_purchased + (b.quantity_in_basket * 0.7) + (b.number_of_postponed * 0.4) ) as popularity " +
                    "from shop.books b LEFT JOIN shop.authors a on b.author_id = a.id " +
                    "order by popularity desc", nativeQuery = true)
    Page<Book> getAllBooksByPopularity(Pageable pageable);

    Page<Book> findBooksByTagsIsContaining(Pageable pageable, Tag tag);

    List<Book> findBooksByTagsIsContaining(Tag tag);
}
