CREATE TABLE tarifs (
    id SERIAL PRIMARY KEY,
    name_tarif TEXT NOT NULL,
    inter_packet_mb INTEGER NOT NULL,
    cost_tarif MONEY NOT NULL
);

INSERT INTO tarifs (name_tarif, inter_packet_mb, cost_tarif)
VALUES
    ('Максимальный', 1000000, 800.00),
    ('Мощный', 100, 400.00),
    ('Честный', 10, 100.00);
	
		
		
		
		
CREATE TABLE services (
    id SERIAL PRIMARY KEY,
    name_service TEXT NOT NULL,
    cost_service MONEY NOT NULL
);

INSERT INTO services (name_service, cost_service)
VALUES
    ('АнтиВирус Касперский', 100.00),
    ('Выделенный IP', 100.00),
    ('Персональный менеджер', 100.00),
    ('Фирменный роутер', 100.00);
	
	
	
	
	
	

CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    num_dog TEXT CHECK (num_dog ~ '^516\d{6}$'),
    fio TEXT NOT NULL,
    telephone VARCHAR(20) DEFAULT NULL,
    mail VARCHAR(100) DEFAULT NULL,
	telegram_id TEXT DEFAULT NULL
);


INSERT INTO customers (num_dog, fio, telephone, mail)
VALUES
    (516123456, 'Иванов Иван Иванович', '+79001234567', 'ivanov@mail.ru'),
    (516789012, 'Петров Петр Петрович', '+79112345678', 'petrov@gmail.com'),
    (516345678, 'Сидорова Мария Ивановна', '+79207654321', 'sidorova@yandex.ru'),
    (516901234, 'Кузнецов Алексей Владимирович', '+79301234567', 'kuznetsov@mail.ru'),
    (516567890, 'Смирнова Елена Николаевна', '+79401234567', 'smirnova@gmail.com'),
    (516123789, 'Соколов Дмитрий Андреевич', '+79501234567', 'sokolov@yandex.ru'),
    (516456789, 'Михайлова Анна Сергеевна', '+79601234567', 'mikhaylova@mail.ru'),
    (516789456, 'Новиков Игорь Владимирович', '+79701234567', 'novikov@gmail.com'),
    (516012345, 'Федорова Ольга Михайловна', '+79801234567', 'fedorova@yandex.ru');
	
	




CREATE TABLE admins (
    id SERIAL PRIMARY KEY,
    login TEXT CHECK (login ~ '^a\w+$'),
    fio TEXT NOT NULL,
    telephone VARCHAR(20),
    mail VARCHAR(100),
	telegram_id TEXT DEFAULT NULL
);


INSERT INTO admins (login, fio, telephone, mail)
VALUES
    ('admin1', 'Иванов Иван Иванович', '+79001234567', 'admin1@example.com'),
    ('admin2', 'Петров Петр Петрович', '+79112345678', 'admin2@example.com');
