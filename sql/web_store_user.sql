CREATE USER 'webstore'@'localhost' IDENTIFIED BY 'webstore_password';

GRANT SELECT, INSERT, UPDATE, DELETE ON web_store.* TO
    'webstore'@'localhost';

FLUSH PRIVILEGES;