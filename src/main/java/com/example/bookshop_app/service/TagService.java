package com.example.bookshop_app.service;

import com.example.bookshop_app.entity.Tag;
import com.example.bookshop_app.repo.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    private TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Page<Tag> getAllTags(int offset, int limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return tagRepository.findAll(nextPage);
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag getTagById(String tagId) {
        return tagRepository.getById(Integer.parseInt(tagId));
    }
}
