
CREATE TABLE requests (
    id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app VARCHAR(256) NOT NULL,
    uri VARCHAR(256) NOT NULL,
    ip VARCHAR(256) NOT NULL,
    request_date TIMESTAMP NOT NULL,
    CONSTRAINT pk_requests PRIMARY KEY (id)
);