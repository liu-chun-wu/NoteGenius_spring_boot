package com.jeffery.notegenius.repository;

import com.jeffery.notegenius.model.Tag;
import com.jeffery.notegenius.model.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@DataJpaTest
@ActiveProfiles("test")
public class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindAllByUserId() {
        User user = new User();
        user.setUsername("tagger");
        user.setEmail("tagger@example.com");
        user.setPassword("abc123");
        userRepository.save(user);

        Tag tag1 = new Tag();
        tag1.setName("工作");
        tag1.setUser(user);
        tagRepository.save(tag1);

        Tag tag2 = new Tag();
        tag2.setName("學習");
        tag2.setUser(user);
        tagRepository.save(tag2);

        List<Tag> tags = tagRepository.findAllByUserId(user.getId());
        assertThat(tags).hasSize(2);
        assertThat(tags).extracting(Tag::getName).contains("工作", "學習");
    }

    @Test
    public void testExistsByUserIdAndName() {
        User user = new User();
        user.setUsername("existtester");
        user.setEmail("exist@test.com");
        user.setPassword("pw123");
        userRepository.save(user);

        Tag tag = new Tag();
        tag.setName("重要");
        tag.setUser(user);
        tagRepository.save(tag);

        boolean exists = tagRepository.existsByUserIdAndName(user.getId(), "重要");
        assertThat(exists).isTrue();

        boolean notExists = tagRepository.existsByUserIdAndName(user.getId(), "不重要");
        assertThat(notExists).isFalse();
    }
}