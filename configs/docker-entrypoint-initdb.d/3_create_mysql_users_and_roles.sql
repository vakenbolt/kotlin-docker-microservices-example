CREATE USER 'nba_finals_service'@'localhost' IDENTIFIED BY 'ilovethisgame';
CREATE USER 'nba_finals_service'@'nba_finals_web_api' IDENTIFIED BY 'ilovethisgame';
GRANT SELECT ON nba_finals.champions TO 'nba_finals_service'@'localhost';
GRANT SELECT ON nba_finals.champions TO 'nba_finals_service'@'nba_finals_web_api';
FLUSH PRIVILEGES;