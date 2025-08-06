CREATE TABLE IF NOT EXISTS product (
	id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS customer (
	id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS sale (
	id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    total_cost DECIMAL(10, 2) NOT NULL,
    created_at DATETIME NOT NULL,
    customer_id BIGINT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE TABLE IF NOT EXISTS sale_item (
	id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    product_id BIGINT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(id),
    sale_id BIGINT NOT NULL,
    FOREIGN KEY (sale_id) REFERENCES sale(id)
);

CREATE TABLE cashier (
	id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    is_active TINYINT(1) NOT NULL
);

CREATE TABLE IF NOT EXISTS shift_log (
	id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    shift_date DATE NOT NULL,
    time_in DATETIME NOT NULL,
    time_out DATETIME NOT NULL,
    opening_balance DOUBLE NOT NULL,
    closing_balance DOUBLE NOT NULL,
    total_sales DOUBLE NOT NULL,
    transaction_count BIGINT NOT NULL,
    cashier_id BIGINT NOT NULL,
    FOREIGN KEY (cashier_id) REFERENCES cashier(id)
);

CREATE TABLE IF NOT EXISTS ingredient (
	id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    quantity BIGINT NOT NULL,
    unit VARCHAR(255) NOT NULL,
    expiration_date DATE NOT NULL,
    low_stock_threshold INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    image_url VARCHAR(255) NOT NULL
);

CREATE TABLE orders (
	transactionNumber BIGINT PRIMARY KEY NOT NULL,
    date DATE NOT NULL,
    time DATETIME NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    cashier_id BIGINT NOT NULL,
    FOREIGN KEY (cashier_id) REFERENCES cashier(id)
);
