package com.jeffery.notegenius.repository;

import com.jeffery.notegenius.model.Note;
import com.jeffery.notegenius.model.NoteImage;
import com.jeffery.notegenius.model.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@DataJpaTest
@ActiveProfiles("test")
public class NoteImageRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NoteImageRepository noteImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveAndFindNoteImages() {
        User user = new User();
        user.setUsername("imguser");
        user.setEmail("img@example.com");
        user.setPassword("img123");
        userRepository.save(user);

        Note note = new Note();
        note.setTitle("有圖筆記");
        note.setContent("這是一篇附圖的筆記");
        note.setUser(user);
        note.setCreatedAt(LocalDateTime.now());
        noteRepository.save(note);

        NoteImage image1 = new NoteImage();
        image1.setNote(note);
        image1.setImage("note_img_1.png");
        image1.setUploadedAt(LocalDateTime.now());

        NoteImage image2 = new NoteImage();
        image2.setNote(note);
        image2.setImage("note_img_2.png");
        image2.setUploadedAt(LocalDateTime.now());

        noteImageRepository.save(image1);
        noteImageRepository.save(image2);

        List<NoteImage> images = noteImageRepository.findAllByNoteId(note.getId());
        assertThat(images).hasSize(2);
        assertThat(images).extracting(NoteImage::getImage).contains("note_img_1.png", "note_img_2.png");
    }
}
