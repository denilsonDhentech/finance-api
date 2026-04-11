CREATE TABLE tb_expenses (
     id UUID PRIMARY KEY,
     description VARCHAR(255) NOT NULL,
     amount DECIMAL(19, 2) NOT NULL,
     due_date DATE NOT NULL,
     type VARCHAR(50) NOT NULL,
     status VARCHAR(50) NOT NULL
);