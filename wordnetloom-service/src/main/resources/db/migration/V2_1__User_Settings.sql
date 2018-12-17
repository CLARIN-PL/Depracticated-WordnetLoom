CREATE TABLE users_settings (
  user_id         BIGINT       NOT NULL,
  lexicon_marker  BOOLEAN,
  chosen_lexicons VARCHAR(255) NOT NULL,
  show_tool_tips  BOOLEAN,
  PRIMARY KEY (user_id)
);

ALTER TABLE users_settings
  ADD CONSTRAINT users_settings_to_user_constraint
FOREIGN KEY (user_id)
REFERENCES users (id);

INSERT INTO users_settings (user_id, lexicon_marker, chosen_lexicons, show_tool_tips)
  SELECT
    u.id,
    TRUE,
    '1;2;3;',
    TRUE
  FROM users u;