INSERT INTO users_settings (user_id, lexicon_marker, chosen_lexicons, show_tool_tips)
  SELECT
    u.id,
    TRUE,
    '1;2;3;',
    TRUE
  FROM users u where u.email = 'anonymous@clarin-pl.eu';

