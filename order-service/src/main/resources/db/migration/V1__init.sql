CREATE TABLE "order"
(
    order_id   BIGSERIAL                NOT NULL PRIMARY KEY,
    status     VARCHAR(32)              NOT NULL,
    amount     NUMERIC(6, 2)            NOT NULL,
    order_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);