package com.jeffery.notegenius.repository;

import com.jeffery.notegenius.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findAllByUserId(Long userId);
    Optional<Tag> findByUserIdAndName(Long userId, String name);
    boolean existsByUserIdAndName(Long userId, String name);
    Optional<Tag> findByIdAndUserId(Long tagId, Long userId);
}
