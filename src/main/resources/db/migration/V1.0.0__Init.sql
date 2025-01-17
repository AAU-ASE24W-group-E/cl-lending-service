CREATE TABLE lending_requests
(
    id         UUID                                               NOT NULL PRIMARY KEY,
    book_id    UUID                                               NOT NULL,
    reader_id  UUID                                               NOT NULL,
    owner_id   UUID                                               NOT NULL,
    status     VARCHAR(255)                                       NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE lending_meetings
(
    id                 UUID                                               NOT NULL PRIMARY KEY,
    lending_request_id UUID                                               NOT NULL,
    meeting_time       TIMESTAMP WITH TIME ZONE                           NOT NULL,
    meeting_place      VARCHAR(255)                                       NOT NULL,
    deadline           TIMESTAMP WITH TIME ZONE,
    created_at         TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (lending_request_id) REFERENCES lending_requests (id) ON DELETE CASCADE
);

CREATE TABLE lending_status_history
(
    id                 UUID                                               NOT NULL PRIMARY KEY,
    lending_request_id UUID                                               NOT NULL,
    status             VARCHAR(255)                                       NOT NULL,
    changed_at         TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (lending_request_id) REFERENCES lending_requests (id) ON DELETE CASCADE
);
