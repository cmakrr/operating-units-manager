ALTER TABLE operation
    ALTER COLUMN o_operation_fact_id DROP NOT NULL;

INSERT INTO operation_info_column (oic_name, oic_column_name)
VALUES ('OPERATION_TIME_INTERVAL', 'Время опер.'),
       ('PATIENT_DETAILS', 'ФИО пациента, возраст'),
       ('PATIENT_ROOM_NUMBER', '№ палаты'),
       ('OPERATION_NAME', 'Название операции'),
       ('MEDICAL_WORKERS', 'Оператор,ассистент,трансфузиолог'),
       ('INSTRUMENTS', 'Материалы, инструменты')
ON CONFLICT DO NOTHING;

INSERT INTO operation_step (os_name, os_number)
VALUES ('Привоз пациента', 1),
       ('Начало операции', 2),
       ('Ввод анестезии', 3),
       ('Начало работы хирурга', 4),
       ('Окончание работы хирурга', 5)
ON CONFLICT DO NOTHING;

INSERT INTO access_role (ar_id, ar_role, ar_description, ar_settable)
VALUES (1, 'TRACKER', 'Оператор', true),
       (2, 'MANAGER', 'Менеджер', true),
       (3, 'GENERAL_MANAGER', 'Главный менеджер', true),
       (4, 'ADMIN', 'Администратор', true)
ON CONFLICT DO NOTHING;

INSERT INTO app_user(u_login, u_role, u_password, u_registration_date)
VALUES ('manager', 2, '$2a$10$nvoniP3uNPbI0tGZ3cImbOZLTiubI8dvJkt4zDVhK4FPgOq8ctCja', CURRENT_DATE),
       ('mainManager', 3, '$2a$10$AHyFX1tYSvekjBf.hvXGz.aX0t/HW1HwdLSF7vNotPrYwIsvqGeY6', CURRENT_DATE),
       ('admin', 4, '$2a$10$fXYAfY1ORb3UCHRiucwEMOZXS6CgXvu2oT8xgK8n1BAvVkaTLCDre', CURRENT_DATE)
ON CONFLICT DO NOTHING;