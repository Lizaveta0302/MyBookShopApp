package org.app.repos;

import org.app.dto.Book;

import java.util.List;

public interface ProjectRepository<T> {
    List<T> retrieveAll();

    void save(T item);

    public void removeItemByField(String itemField, String itemValue);

    public List<T> filterItemsByField(String itemField, String itemValue);

}
