CREATE TABLE shipment
(
    shipment_id BIGSERIAL                NOT NULL PRIMARY KEY,
    order_id    BIGSERIAL                NOT NULL,
    order_date  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);