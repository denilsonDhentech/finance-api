-- Adiciona as colunas (neste momento, a tabela expenses deve estar vazia)
ALTER TABLE tb_expenses
    ADD COLUMN user_id UUID NOT NULL,
ADD COLUMN category_id UUID NOT NULL;

-- Cria o relacionamento com tb_users
ALTER TABLE tb_expenses
    ADD CONSTRAINT fk_expense_user
        FOREIGN KEY (user_id) REFERENCES tb_users (id);

-- Cria o relacionamento com tb_categories
ALTER TABLE tb_expenses
    ADD CONSTRAINT fk_expense_category
        FOREIGN KEY (category_id) REFERENCES tb_categories (id);