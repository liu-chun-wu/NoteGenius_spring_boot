package com.jeffery.notegenius.repository;

import com.jeffery.notegenius.model.NoteImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteImageRepository extends JpaRepository<NoteImage, Long> {
    List<NoteImage> findAllByNoteId(Long noteId);
}
