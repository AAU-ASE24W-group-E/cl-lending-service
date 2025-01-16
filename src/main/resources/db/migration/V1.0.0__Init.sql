-- TODO FK handling
CREATE TABLE lending_requests
(
    id         UUID        NOT NULL PRIMARY KEY,
    book_id    INT         NOT NULL,
    reader_id  UUID        NOT NULL,
    owner_id   UUID        NOT NULL,
    status     VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
--     CONSTRAINT status_check CHECK (status IN ('pending', 'accepted', 'rejected', 'completed'))
);

CREATE TABLE lending_meetings
(
    id                 UUID         NOT NULL PRIMARY KEY,
    lending_request_id UUID         NOT NULL,
    meeting_time       TIMESTAMP    NOT NULL,
    meeting_place      VARCHAR(255) NOT NULL,
    deadline           TIMESTAMP,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (lending_request_id) REFERENCES lending_requests (id) ON DELETE CASCADE
);

CREATE TABLE lending_status_history
(
    id                 UUID        NOT NULL PRIMARY KEY,
    lending_request_id UUID         NOT NULL,
    status             VARCHAR(20) NOT NULL,
    changed_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (lending_request_id) REFERENCES lending_requests (id) ON DELETE CASCADE,
--     CONSTRAINT status_check_history CHECK (status IN ('pending', 'accepted', 'rejected', 'completed'))
);
