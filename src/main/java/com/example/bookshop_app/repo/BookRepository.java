package com.example.bookshop_app.repo;

import com.example.bookshop_app.entity.Genre;
import com.example.bookshop_app.entity.Tag;
import com.example.bookshop_app.entity.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query(value = "select *" +
            " from shop.books b where is_bestseller = true " +
            " order by (select count(*) from shop.visits v where v.book_id = b.id and v.visited_at >= NOW() - INTERVAL '7 DAY') desc", nativeQuery = true)
    Page<Book> findAllByIsBestsellerTrue(Pageable pageable);

    Page<Book> findAllByOrderByPubDateDesc(Pageable pageable);

    Page<Book> findAllByPubDateBetweenOrderByPubDateDesc(Pageable pageable, Date from, Date to);

    @Query(value = "select *, (b.number_of_purchased + (b.quantity_in_basket * 0.7) + (b.number_of_postponed * 0.4) ) as popularity " +
            "from shop.books b LEFT JOIN shop.authors a on b.author_id = a.id " +
            "order by popularity desc", nativeQuery = true)
    Page<Book> getAllBooksByPopularity(Pageable pageable);

    Page<Book> findBooksByTagsIsContaining(Pageable pageable, Tag tag);

    Page<Book> findBooksByGenresIsContaining(Pageable nextPage, Genre genre);

    Book findBookBySlug(String slug);

    List<Book> findBooksBySlugIn(String[] cookieSlugs);

    @Modifying
    @Query(value = "UPDATE shop.books b SET number_of_postponed=:numberOfPostponed WHERE b.slug = :slug", nativeQuery = true)
    void updateNumberOfPostponed(@Param("slug") String slug, @Param("numberOfPostponed") Integer numberOfPostponed);

    @Modifying
    @Query(value = "UPDATE shop.books b SET quantity_in_basket=:newQuantity WHERE b.slug = :slug", nativeQuery = true)
    void updateQuantityInBasket(@Param("slug") String slug, @Param("newQuantity") Integer newQuantity);

}
