package com.example.bookshop_app.service;

import com.example.bookshop_app.entity.Visit;
import com.example.bookshop_app.repo.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VisitService {

    private VisitRepository visitRepository;

    @Autowired
    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    public void insertVisit(Visit visit) {
        visitRepository.saveAndFlush(visit);
    }

    public Visit getByUniqueKey(Integer userId, Integer bookId) {
        return visitRepository.findByUserIdAndAndBookId(userId, bookId);
    }

    @Transactional
    public void updateVisitedAt(Long id, LocalDateTime visitedAt) {
        visitRepository.updateVisitedAt(id, visitedAt);
    }

    public List<Visit> getRecentVisits(LocalDateTime lowLimitDate, Integer userId) {
        return visitRepository.findAllByVisitedAtAfterAndUserIdOrderByVisitedAt(lowLimitDate, userId);
    }
}
