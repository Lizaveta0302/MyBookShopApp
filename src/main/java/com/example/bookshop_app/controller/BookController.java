package com.example.bookshop_app.controller;

import com.example.bookshop_app.data.ResourceStorage;
import com.example.bookshop_app.dto.BooksPageDto;
import com.example.bookshop_app.dto.SearchWordDto;
import com.example.bookshop_app.entity.Genre;
import com.example.bookshop_app.entity.Tag;
import com.example.bookshop_app.entity.book.Book;
import com.example.bookshop_app.entity.book.BookMark;
import com.example.bookshop_app.entity.book.review.BookReview;
import com.example.bookshop_app.entity.book.review.BookReviewLike;
import com.example.bookshop_app.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequestMapping("/books")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private TagService tagService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private BookMarkService bookMarkService;
    @Autowired
    private BookReviewService bookReviewService;

    private final BookService bookService;
    private final ResourceStorage storage;

    @Autowired
    public BookController(BookService bookService, ResourceStorage storage) {
        this.bookService = bookService;
        this.storage = storage;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @GetMapping("/recent/page")
    public String getRecentBooksPage(Model model) {
        BooksPageDto recentBooksPageDto = new BooksPageDto(bookService.getPageOfRecentBooks(0, 6).getContent());
        model.addAttribute("recentBooks", recentBooksPageDto.getBooks());
        return "/books/recent";
    }

    @GetMapping("/popular/page")
    public String getPopularBooksPage(Model model) {
        BooksPageDto popularBooksPageDto = new BooksPageDto(bookService.getPageOfPopularBooks(0, 6).getContent());
        model.addAttribute("popularBooks", popularBooksPageDto.getBooks());
        return "/books/popular";
    }

    @GetMapping("/recommended")
    @ResponseBody
    public BooksPageDto getRecommendedBooksPage(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit, Model model) {
        return new BooksPageDto(bookService.getPageOfRecommendedBooks(offset, limit).getContent());
    }

    @GetMapping("/recent")
    @ResponseBody
    public BooksPageDto getRecentBooksPageWithPeriod(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit,
                                                     @RequestParam("from") String from, @RequestParam("to") String to) throws ParseException {
        return new BooksPageDto(bookService.getPageOfRecentBooksWithPeriod(offset, limit, from, to).getContent());
    }

    @GetMapping("/popular")
    @ResponseBody
    public BooksPageDto getPopularBooksPage(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfPopularBooks(offset, limit).getContent());
    }

    @GetMapping("/author/{authorId}")
    @ResponseBody
    public BooksPageDto allPageOfBooksByAuthorId(@PathVariable String authorId, @RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getBooksByAuthorId(offset, limit, authorId).getContent());
    }

    @GetMapping("/authors/{authorId}")
    public String getAllBooksByAuthorId(@PathVariable String authorId, Model model) {
        model.addAttribute("authorBooks", bookService.getBooksByAuthorId(0, 6, authorId).getContent());
        return "/books/author";
    }

    @GetMapping("/{id}")
    public String getBookById(@PathVariable String id, Model model) {
        Book book = bookService.getBookById(id);
        fillModel(model, book);
        return "/books/slug";
    }

    @GetMapping("/slug/{slug}")
    public String bookPage(@PathVariable("slug") String slug, Model model) {
        Book book = bookService.findBookBySlug(slug);
        fillModel(model, book);
        return "/books/slug";
    }

    @GetMapping("/tags/{tagId}")
    public String getBooksByTag(@PathVariable String tagId, Model model) {
        Tag tag = tagService.getTagById(tagId);
        model.addAttribute("tag", tag);
        model.addAttribute("booksByTag", bookService.getBooksByTag(0, 6, tag).getContent());
        return "/tags/index";
    }

    @GetMapping("/genres/{genreId}")
    public String getBooksByGenre(@PathVariable String genreId, Model model) {
        Genre genre = genreService.getGenreById(genreId);
        model.addAttribute("genre", genre);
        model.addAttribute("booksByGenre", bookService.getBooksByGenre(0, 6, genre).getContent());
        return "/genres/slug";
    }

    @PostMapping("/{slug}/img/save")
    public String saveNewBookImage(@RequestParam("file") MultipartFile file, @PathVariable("slug") String slug) throws IOException {
        String savePath = storage.saveNewBookImage(file, slug);
        Book bookToUpdate = bookService.getBookBySlug(slug);
        bookToUpdate.setImage(savePath);
        bookService.save(bookToUpdate);
        return ("redirect:/books/" + bookToUpdate.getId());
    }

    @GetMapping("/download/{hash}")
    public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("hash") String hash) throws IOException {
        Path path = storage.getBookFilePath(hash);
        logger.info("book file path: {}", path);

        MediaType mediaType = storage.getBookFileMime(hash);
        logger.info("book file mime type: {}", mediaType);

        byte[] data = storage.getBookFileByteArray(hash);
        logger.info("book file data len: {}", data.length);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

    @Secured("ROLE_USER")
    @PostMapping("/changeBookStatus/rate")
    public String handleMovingBookToPostponedFromCart(@RequestParam("bookId") Integer bookId, @RequestParam("value") Integer mark) {
        BookMark rate = new BookMark();
        rate.setBook(bookService.getBookById(bookId.toString()));
        rate.setMark(mark.shortValue());
        bookMarkService.insertBookRate(rate);
        return "redirect:/books/" + bookId;
    }

    @Secured("ROLE_USER")
    @PostMapping("/rateBookReview")
    public String handleRatingBookReview(@RequestBody Map<String, String> payload) {
        BookReview bookReview = bookReviewService.getBookReviewById(Long.parseLong(payload.get("reviewId")));
        BookReviewLike like = new BookReviewLike();
        like.setReview(bookReview);
        like.setTime(LocalDateTime.now());
        int likeValue = Integer.parseInt(payload.get("value"));
        like.setValue(likeValue);
        if (likeValue == 1) {
            bookReviewService.insertBookReviewLike(like);
        } else if (likeValue == 0) {
            bookReviewService.deleteBookReviewLike(bookReview.getId(), null);
        } else if (likeValue == -1) {
            like.setValue(0);
            bookReviewService.insertBookReviewLike(like);
        }
        return "redirect:/books/" + bookReview.getBook().getId();
    }

    private void fillModel(Model model, Book book) {
        model.addAttribute("book", book);
        Map<Integer, Integer> bookRatingMap = bookMarkService.getBookRating(book.getId());
        model.addAttribute("bookRatingMap", bookRatingMap);
        model.addAttribute("bookAverageRating", bookRatingMap.get(6));
        model.addAttribute("bookReviews", bookReviewService.getAllBookReviews(book));
    }
}
