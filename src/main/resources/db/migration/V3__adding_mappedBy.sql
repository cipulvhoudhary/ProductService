ALTER TABLE categories_products
DROP
FOREIGN KEY fk_catpro_on_category;

ALTER TABLE categories_products
DROP
FOREIGN KEY fk_catpro_on_product;

DROP TABLE categories_products;