package com.nexters.phochak.repository;

import com.nexters.phochak.domain.ReportPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportPostRepository extends JpaRepository<ReportPost, Long> {
    Long countByPost_Id(Long id);
}
