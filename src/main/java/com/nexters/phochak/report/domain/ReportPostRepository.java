package com.nexters.phochak.report.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportPostRepository extends JpaRepository<ReportPost, Long> {
    Long countByPost_Id(Long id);
}
