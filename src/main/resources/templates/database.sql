CREATE TABLE category (
                          category_id BIGINT NOT NULL AUTO_INCREMENT,
                          title VARCHAR(1023) NOT NULL,
                          subtitle VARCHAR(2047),
                          parent_id BIGINT,
                          owner_id BIGINT NOT NULL,
                          creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT category_pk PRIMARY KEY(category_id),
                          CONSTRAINT category_category_fk FOREIGN KEY(parent_id) REFERENCES category(category_id),
                          CONSTRAINT category_user_fk FOREIGN KEY(owner_id) REFERENCES user(user_id)
);

CREATE TABLE thread(
                       thread_id BIGINT NOT NULL AUTO_INCREMENT,
                       tree_node_id BIGINT,
                       title VARCHAR(1023) NOT NULL,
                       subtitle VARCHAR(2047) NOT NULL,
                       owner_id BIGINT NOT NULL,
                       creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT thread_pk PRIMARY KEY(thread_id),
                       CONSTRAINT thread_user_fk FOREIGN KEY(owner_id) REFERENCES user(user_id),
                       CONSTRAINT thread_category_fk FOREIGN KEY(tree_node_id) REFERENCES category(category_id)
);

CREATE TABLE user(
                     user_id BIGINT NOT NULL AUTO_INCREMENT,
                     username VARCHAR(255) NOT NULL,
                     password VARCHAR(1023) NOT NULL,
                     CONSTRAINT user_pk PRIMARY KEY(user_id)
);