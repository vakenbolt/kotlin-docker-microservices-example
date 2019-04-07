CREATE USER 'nba_finals_service'@'localhost' IDENTIFIED BY 'ilovethisgame';
GRANT SELECT ON nba_finals.champions TO 'nba_finals_service'@'localhost';
FLUSH PRIVILEGES;