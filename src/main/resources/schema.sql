create table if not exists users(
    id bigint auto_increment primary key,
    email varchar(100) not null,
    login varchar(50) not null,
    name varchar(50),
    birthday date not null,
    unique(email)
);

create table if not exists friends(
    userId bigint,
    friendId bigint,
    foreign key (userId) references users(id),
    foreign key (friendId) references users(id),
    unique (userId, friendId)
);
create index if not exists friends on friends(userId);
create index if not exists friends on friends(friendId);

create table if not exists mpa(
    id integer primary key,
    code varchar(10) not null
);

create table if not exists films(
    id bigint auto_increment primary key,
    name varchar(100) not null,
    description varchar(200) not null,
    release_date date not null,
    duration integer not null,
    mpa_id integer,
    foreign key (mpa_id) references mpa(id)
);
create index if not exists films_i01 on films(mpa_id);

create table if not exists likes(
    film_id bigint,
    user_id bigint,
    foreign key (film_id) references films(id),
    foreign key (user_id) references users(id),
    unique (film_id, user_id)
);
create index if not exists likes_i01 on likes(film_id);
create index if not exists likes_i02 on likes(user_id);

create table if not exists genres(
    id integer primary key,
    name varchar(50) not null
);

create table if not exists film_genres(
    film_id bigint,
    genre_id integer,
    foreign key (film_id) references films(id),
    foreign key (genre_id) references genres(id)
);
create index if not exists film_genres_i01 on film_genres(film_id);
create index if not exists film_genres_i02 on film_genres(genre_id);
