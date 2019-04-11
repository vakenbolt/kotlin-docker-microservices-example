CREATE USER 'nba_finals_service'@'localhost' IDENTIFIED BY 'ilovethisgame';
CREATE USER 'nba_finals_service'@'%' IDENTIFIED BY 'ilovethisgame';
GRANT SELECT ON nba_finals.champions TO 'nba_finals_service'@'localhost';
GRANT SELECT ON nba_finals.champions TO 'nba_finals_service'@'%';
GRANT SELECT ON nba_finals.users TO 'nba_finals_service'@'localhost';
GRANT SELECT ON nba_finals.users TO 'nba_finals_service'@'%';
GRANT INSERT ON nba_finals.users TO 'nba_finals_service'@'localhost';
GRANT INSERT ON nba_finals.users TO 'nba_finals_service'@'%';
FLUSH PRIVILEGES;