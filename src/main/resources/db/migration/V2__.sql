CREATE TABLE categories_products
(
    categories_id BIGINT NOT NULL,
    products_id   BIGINT NOT NULL
);

ALTER TABLE categories_products
    ADD CONSTRAINT uc_categories_products_products UNIQUE (products_id);

ALTER TABLE categories
    ADD CONSTRAINT uc_categories_title UNIQUE (title);

ALTER TABLE categories_products
    ADD CONSTRAINT fk_catpro_on_category FOREIGN KEY (categories_id) REFERENCES categories (id);

ALTER TABLE categories_products
    ADD CONSTRAINT fk_catpro_on_product FOREIGN KEY (products_id) REFERENCES products (id);

DROP TABLE categories_seq;

DROP TABLE products_seq;

ALTER TABLE st_users
    MODIFY number_ofhrs INT NOT NULL;

ALTER TABLE categories
    MODIFY title VARCHAR (255) NOT NULL;

ALTER TABLE st_users
    MODIFY user_type INT NULL;