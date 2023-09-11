package com.nexters.phochak.post.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportPostRepository extends JpaRepository<ReportPost, Long> {
    Long countByPost_Id(Long id);
}
