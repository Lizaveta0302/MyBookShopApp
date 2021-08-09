package org.app.repository;

import java.util.List;

public interface ProjectRepository<T> {
    List<T> retrieveAll();

    void save(T item);

    void removeItemByField(String itemField, String itemValue);

}
