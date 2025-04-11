delete from genres where id not in (1,2,3,4,5,6);

MERGE INTO genres (id, name) KEY(id) values
               (1, 'Комедия'),
               (2, 'Драма'),
               (3, 'Мультфильм'),
               (4, 'Триллер'),
               (5, 'Документальный'),
               (6, 'Боевик');

MERGE INTO mpa (id, code) KEY(id) values
     (1, 'G'),
     (2, 'PG'),
     (3, 'PG-13'),
     (4, 'R'),
     (5, 'NC-17');