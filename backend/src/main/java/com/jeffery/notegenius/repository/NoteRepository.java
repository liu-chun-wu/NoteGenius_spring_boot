package com.jeffery.notegenius.repository;

import com.jeffery.notegenius.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByUserId(Long userId);
    Optional<Note> findByIdAndUserId(Long id, Long userId);
}
