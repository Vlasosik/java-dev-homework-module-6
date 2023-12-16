INSERT INTO worker (ID, NAME, BIRTHDAY, LEVEL, SALARY)
    VALUES 
        (1, 'Nazar', '1999-02-11', 'MIDDLE', 2000.00),
        (2, 'Vlas', '2001-08-31', 'JUNIOR', 1000.00),
        (3, 'Pavlo', '1998-03-31', 'TRAINEE', 1000.00),
        (4,'Denys', '1995-06-12', 'SENIOR', 4000.00),
        (5,'Dmytro', '2002-02-19', 'JUNIOR', 1000.00),
        (6,'Volodymyr', '1990-11-27', 'MIDDLE', 3000.00),
        (7,'Sergei', '1995-12-04', 'SENIOR', 3500.00),
        (8,'Oleksandr', '2002-05-28', 'TRAINEE', 1000.00),
        (9,'Vadym', '1996-04-30', 'SENIOR',4000.00),
        (10,'Oleg', '2003-09-15', 'JUNIOR', 1000.00);

INSERT INTO client (ID, NAME) 
    VALUES
        (1, 'Oliver'),
        (2,'Harry'),
        (3,'Charlie'),
        (4,'Thomas'),
        (5,'Oscar');

INSERT INTO project (ID, CLIENT_ID, START_DATE, FINISH_DATE)
    VALUES
        (1, 1, '2020-01-01 08:30:00', '2023-04-01 17:45:30'),
        (2, 2, '2022-02-15 12:00:00', '2022-09-15 15:30:00'),
        (3, 3, '2021-03-10 09:15:00', '2022-06-10 18:00:00'),
        (4, 4, '2020-04-05 10:45:00', '2022-08-05 16:20:45'),
        (5, 5, '2016-05-20 14:20:00', '2022-07-20 13:45:00'),
        (6, 3, '2020-06-15 11:30:00', '2022-12-15 20:10:00'),
        (7, 2, '2019-07-01 08:00:00', '2022-10-01 14:35:00'),
        (8, 4, '2017-08-08 13:45:00', '2023-02-08 17:00:00'),
        (9, 1, '2018-09-25 16:30:00', '2023-11-25 19:45:00'),
        (10, 3, '2022-10-10 09:00:00', '2023-01-10 14:15:00');

INSERT INTO project_worker (PROJECT_ID, WORKER_ID)
    VALUES 
        (1, 9),
        (2, 5),
        (3, 7),
        (4, 8),
        (1, 2);



