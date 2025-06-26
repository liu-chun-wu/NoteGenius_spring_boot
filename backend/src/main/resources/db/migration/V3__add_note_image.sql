CREATE TABLE note_images (
                             id BIGSERIAL PRIMARY KEY,
                             note_id BIGINT NOT NULL,
                             image VARCHAR(255) NOT NULL,
                             uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             CONSTRAINT fk_noteimage_note FOREIGN KEY (note_id) REFERENCES notes (id) ON DELETE CASCADE
);
