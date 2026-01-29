CREATE TABLE finalization (
    id UUID PRIMARY KEY,
    task_id UUID NOT NULL,
    finalized_at TIMESTAMP,
    outcome VARCHAR(50) NOT NULL
);

CREATE TABLE finalization_event (
    id SERIAL PRIMARY KEY,
    finalization_id UUID NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP,
    CONSTRAINT finalization_fk foreign key (finalization_id) references finalization (id)
);

