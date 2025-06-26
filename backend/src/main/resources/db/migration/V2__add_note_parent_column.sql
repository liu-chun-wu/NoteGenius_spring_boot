-- 建立測試用使用者
INSERT INTO users (username, email, password, created_at)
VALUES
    ('alice', 'alice@example.com', 'password123', CURRENT_TIMESTAMP),
    ('bob', 'bob@example.com', 'password456', CURRENT_TIMESTAMP);

-- 建立標籤
INSERT INTO tags (name, user_id)
VALUES
    ('work', 1),
    ('study', 1),
    ('fun', 1),
    ('python', 2),
    ('travel', 2),
    ('music', 2);

-- 建立筆記
INSERT INTO notes (user_id, title, content, created_at)
VALUES
    (1, '工作筆記', '今天處理了很多 Jira 任務', CURRENT_TIMESTAMP),
    (1, '學習筆記', '複習了演算法與資料結構', CURRENT_TIMESTAMP),
    (2, '旅遊日記', '想去北海道看雪祭', CURRENT_TIMESTAMP);

-- 綁定筆記與標籤（多對多）
INSERT INTO note_tags (note_id, tag_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 5);
