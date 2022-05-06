package com.example.bookshop_app.repo;

import com.example.bookshop_app.entity.BookstoreUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookstoreUserRepository extends JpaRepository<BookstoreUser, Integer> {

    BookstoreUser findBookstoreUserByEmail(String email);

    BookstoreUser findBookstoreUserByPhone(String phone);

    @Modifying
    @Query(value = "UPDATE shop.users SET name=:name, email=:email, phone=:phone, password=:password" +
            " WHERE id=:userId", nativeQuery = true)
    void updateUserProfile(@Param("name") String name, @Param("email") String email, @Param("phone") String phone,
                           @Param("password") String password, @Param("userId") Integer userId);

    BookstoreUser findBookstoreUserById(Integer userId);

    @Modifying
    @Query(value = "UPDATE shop.users SET balance=:balance" +
            " WHERE id=:userId", nativeQuery = true)
    void updateUserBalance(@Param("balance") Double balance, @Param("userId") Integer userId);

}
