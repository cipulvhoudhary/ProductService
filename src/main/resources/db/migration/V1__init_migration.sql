CREATE TABLE categories
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    created_at       datetime NULL,
    last_modified_at datetime NULL,
    title            VARCHAR(255) NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE categories_seq
(
    next_val BIGINT NULL
);

CREATE TABLE instructor
(
    id       BIGINT NOT NULL,
    email    VARCHAR(255) NULL,
    name     VARCHAR(255) NULL,
    password VARCHAR(255) NULL,
    rating DOUBLE NULL,
    subject  VARCHAR(255) NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE jt_instructors
(
    rating DOUBLE NULL,
    subject VARCHAR(255) NULL,
    id      BIGINT NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE jt_mentors
(
    company_name VARCHAR(255) NULL,
    id           BIGINT NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE jt_teaching_assistants
(
    number_ofhrs INT    NOT NULL,
    id           BIGINT NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE jt_users
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    email    VARCHAR(255) NULL,
    name     VARCHAR(255) NULL,
    password VARCHAR(255) NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE mentor
(
    id           BIGINT NOT NULL,
    email        VARCHAR(255) NULL,
    name         VARCHAR(255) NULL,
    password     VARCHAR(255) NULL,
    company_name VARCHAR(255) NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE products
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    created_at       datetime NULL,
    last_modified_at datetime NULL,
    `description`    VARCHAR(255) NULL,
    image_url        VARCHAR(255) NULL,
    price DOUBLE NULL,
    title            VARCHAR(255) NULL,
    category_id      BIGINT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE products_seq
(
    next_val BIGINT NULL
);

CREATE TABLE st_users
(
    user_type    INT    NOT NULL,
    id           BIGINT NOT NULL,
    email        VARCHAR(255) NULL,
    name         VARCHAR(255) NULL,
    password     VARCHAR(255) NULL,
    rating DOUBLE NULL,
    subject      VARCHAR(255) NULL,
    company_name VARCHAR(255) NULL,
    number_ofhrs INT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE teaching_assistant
(
    id           BIGINT NOT NULL,
    email        VARCHAR(255) NULL,
    name         VARCHAR(255) NULL,
    password     VARCHAR(255) NULL,
    number_ofhrs INT    NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE tpc_instructors
(
    id       BIGINT NOT NULL,
    email    VARCHAR(255) NULL,
    name     VARCHAR(255) NULL,
    password VARCHAR(255) NULL,
    rating DOUBLE NULL,
    subject  VARCHAR(255) NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE tpc_mentors
(
    id           BIGINT NOT NULL,
    email        VARCHAR(255) NULL,
    name         VARCHAR(255) NULL,
    password     VARCHAR(255) NULL,
    company_name VARCHAR(255) NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE tpc_teaching_assistants
(
    id           BIGINT NOT NULL,
    email        VARCHAR(255) NULL,
    name         VARCHAR(255) NULL,
    password     VARCHAR(255) NULL,
    number_ofhrs INT    NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE tpc_users
(
    id       BIGINT NOT NULL,
    email    VARCHAR(255) NULL,
    name     VARCHAR(255) NULL,
    password VARCHAR(255) NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

ALTER TABLE jt_instructors
    ADD CONSTRAINT FK10cbbbh3y08u4c9ya1uttn1as FOREIGN KEY (id) REFERENCES jt_users (id) ON DELETE NO ACTION;

ALTER TABLE jt_teaching_assistants
    ADD CONSTRAINT FKaqaxtstl1h2et8x8yscifx7qj FOREIGN KEY (id) REFERENCES jt_users (id) ON DELETE NO ACTION;

ALTER TABLE products
    ADD CONSTRAINT FKog2rp4qthbtt2lfyhfo32lsw9 FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE NO ACTION;

CREATE INDEX FKog2rp4qthbtt2lfyhfo32lsw9 ON products (category_id);

ALTER TABLE jt_mentors
    ADD CONSTRAINT FKp3057b7l9glhek7cfjkre3exr FOREIGN KEY (id) REFERENCES jt_users (id) ON DELETE NO ACTION;