-- Таблица типов продуктов
CREATE TABLE product_types (
    type_id SERIAL PRIMARY KEY,
    type_name TEXT UNIQUE NOT NULL
);	

-- Заполнение таблицы типами продуктов
INSERT INTO product_types (type_name)
VALUES
    ('Банка'),
    ('Фрукт'),
    ('Выпечка');

-- Таблица продуктов
CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name_product TEXT NOT NULL,
    cost_product MONEY NOT NULL,
	type_id INT NOT null,
	FOREIGN KEY (type_id) 
  REFERENCES product_types(type_id)
);

-- Заполнение таблицы продуктов
INSERT INTO products (name_product, cost_product, type_id)
VALUES
    ('Адреналин', 90.00, 1),
    ('Яблоко', 400.00, 2),
    ('Булка', 100.00, 3);


-- Таблица с координатами объектов (например полок или человечков)
CREATE TABLE obj_location (
    obj_id SERIAL PRIMARY KEY,
    x_cord FLOAT NOT NULL,
    y_cord FLOAT NOT NULL
);


-- Таблица того чем наполнен объект
CREATE TABLE obj_content (
    id_content SERIAL PRIMARY KEY,
	name_obj INT NOT NULL,
    on_shelf INT DEFAULT NULL,
	count_on INT DEFAULT 0,
	FOREIGN KEY (on_shelf) REFERENCES products(id),
	FOREIGN KEY (name_obj) REFERENCES obj_location(obj_id)
);

-- Таблица длинн путей до точек интереса
CREATE TABLE lens (
    id_content SERIAL PRIMARY KEY,
	name_obj INT NOT NULL,
    x_fin FLOAT DEFAULT NULL,
	y_fin FLOAT DEFAULT NULL,
	FOREIGN KEY (name_obj) REFERENCES obj_location(obj_id)
);
	
		
