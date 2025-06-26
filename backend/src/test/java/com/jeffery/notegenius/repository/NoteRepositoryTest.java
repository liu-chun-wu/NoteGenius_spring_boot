package com.jeffery.notegenius.repository;

import com.jeffery.notegenius.model.Note;
import com.jeffery.notegenius.model.Tag;
import com.jeffery.notegenius.model.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@DataJpaTest
@ActiveProfiles("test")
public class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    public void testFindAllByUserId() {
        User user = new User();
        user.setUsername("noteuser");
        user.setEmail("note@test.com");
        user.setPassword("pass123");
        userRepository.save(user);

        Note note1 = new Note();
        note1.setTitle("筆記一");
        note1.setContent("內容一");
        note1.setCreatedAt(LocalDateTime.now());
        note1.setUser(user);
        noteRepository.save(note1);

        Note note2 = new Note();
        note2.setTitle("筆記二");
        note2.setContent("內容二");
        note2.setCreatedAt(LocalDateTime.now());
        note2.setUser(user);
        noteRepository.save(note2);

        List<Note> notes = noteRepository.findAllByUserId(user.getId());
        assertThat(notes).hasSize(2);
    }

    @Test
    public void testFindByIdAndUserId() {
        User user = new User();
        user.setUsername("lookupuser");
        user.setEmail("lookup@test.com");
        user.setPassword("lookup123");
        userRepository.save(user);

        Note note = new Note();
        note.setTitle("查詢筆記");
        note.setContent("查詢內容");
        note.setCreatedAt(LocalDateTime.now());
        note.setUser(user);
        noteRepository.save(note);

        Optional<Note> result = noteRepository.findByIdAndUserId(note.getId(), user.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("查詢筆記");

        Optional<Note> notFound = noteRepository.findByIdAndUserId(note.getId(), 999L);
        assertThat(notFound).isNotPresent();
    }
}