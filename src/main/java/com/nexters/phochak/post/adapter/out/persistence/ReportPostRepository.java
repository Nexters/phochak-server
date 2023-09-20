package com.nexters.phochak.post.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportPostRepository extends JpaRepository<ReportPostEntity, Long> {
    int countByPostId(Long id);
}
