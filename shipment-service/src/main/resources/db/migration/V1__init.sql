CREATE TABLE shipment
(
    shipment_id BIGSERIAL                NOT NULL PRIMARY KEY,
    order_id    BIGSERIAL                NOT NULL UNIQUE,
    order_date  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);