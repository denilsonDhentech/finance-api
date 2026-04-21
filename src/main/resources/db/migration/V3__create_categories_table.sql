CREATE TABLE tb_categories (
   id UUID PRIMARY KEY,
   name VARCHAR(100) NOT NULL UNIQUE,
   description VARCHAR(255)
);