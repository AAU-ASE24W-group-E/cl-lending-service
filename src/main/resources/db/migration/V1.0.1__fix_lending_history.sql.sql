CREATE TABLE lending_history
(
    id                 UUID                                               NOT NULL PRIMARY KEY,
    lending_request_id UUID                                               NOT NULL,
    status             VARCHAR(255)                                       NOT NULL,
    changed_at         TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (lending_request_id) REFERENCES lending_requests (id) ON DELETE CASCADE
);