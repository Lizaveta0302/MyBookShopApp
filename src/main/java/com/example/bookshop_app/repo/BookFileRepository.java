package com.example.bookshop_app.repo;

import com.example.bookshop_app.entity.book.file.BookFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookFileRepository extends JpaRepository<BookFile, Integer> {
    BookFile findBookFileByHash(String hash);
}
