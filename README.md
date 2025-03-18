# java-filmorate

## Схема БД

![db-schema](db-schema.png)

<details><summary>Код схемы в plantuml</summary>

```plantuml
!define table(name, desc) class name as "desc" << (T, white) >>

!define pk(x, t) <b><color:#red><&key></color> x :</b> <i>t</i>
!define fk(x, t) <color:#orange><&key></color> x : <i>t</i>
!define nn(x, t) <color:#blue><&media-record></color> x : <i>t</i>
!define col(x, t) <color:#efefef><&media-record></color> x : <i>t</i>

skinparam packageStyle rectangle
hide stereotypes
allowmixing

legend top left
<b>Легенда</b>
----
pk("Primary Key", "Type")
fk("Foreign Key", "Type")
nn("Not Null Value", "Type")
col("Value", Type)
end legend

table(users, "user") {
    pk(id, int8)
    ----
    nn(email, varchar[100]) 
    ----
    nn(login, varchar[50]) 
    ----
    col(name, varchar[50])
    ----
    nn(birthday, date)
}

table(requests, "friend_requests") {
    fk(requester_id, int8)
    ----
    fk(replier_id, int8)
    ----
    ----
    <color:blue>unique index friend_requests_ui01 (requester_id, replier_id)</color>
}

table(friends, "confirmed_friends") {
    fk(user1, int8)
    ----
    fk(user2, int8)
    ----
    ----
    <color:blue>unique index confirmed_friends_ui01 (user1, user2)</color>
}

table(films, "films") {
    pk(id, int8)
    ----
    nn(name, varchar[100]) 
    ----
    nn(description, varchar[100])
    ----
    nn(release_date, date) 
    ----
    nn(duration, int)
    ----
    fk(rating_id, int)
}

table(likes, "likes") {
    fk(film_id, int8)
    ----
    fk(user_id, int8)
    ----
    ----
    <color:blue>unique index likes_ui01(film_id, user_id)</color>
}

table(fg, "film_genres") {
    fk(film_id, int8)
    ----
    fk(genre_id, int)
}

table(genre, "genre") {
    pk(id, int)
    ----
    nn(name, varchar[50])
}

table(rating, "rating") {
    pk(id, int)
    ----
    nn(code, varchar[10])
}

requests::requester_id }o..|| users::id
requests::replier_id }o..|| users::id

friends::user1 }o..|| users::id
friends::user2 }o..|| users::id

films::id ||..o{ likes::film_id
users::id ||..o{ likes::user_id

films::rating_id }..|| rating::id

films::id ||.r.o{ fg::film_id
fg::genre_id }o..|| genre::id

```

</details>

## Примеры запросов

### По пользователям

<details><summary>• Выбрать всех пользователей</summary>

```sql
select *
  from users;
```

</details>

<details><summary>• Выбрать пользователя по id</summary>

```sql
select *
  from users
 where id = :id;
```

</details>

<details><summary>• Найти заявки на друзья отправленные пользователем</summary>

```sql
select *
  from friend_requests
 where requester_id = :id;
```

</details>

<details><summary>• Найти всех друзей пользователя</summary>

```sql
select *
  from confirmed_friends
 where user1 = :id;
```

</details>

### По фильмам

<details><summary>• Выбрать все фильмы</summary>

```sql
select *
  from films
 where id = :id;
```

</details>

<details><summary>• Выбрать фильм по id</summary>

```sql
select *
  from films
 where id = :id;
```

</details>

<details><summary>• Найти жанры фильма</summary>

```sql
select r.code
  from film_genres fg
  join genres g on g.id = fg.genre_id
 where fg.film_id = :id;
```

</details>

<details><summary>• Найти рейтинг фильма</summary>

```sql
select r.code
  from films f
  join raiting r on r.id = f.raiting_id
 where id = :id;
```

</details>

<details><summary>• Найти количество лайков фильма</summary>

```sql
select count(1)
  from films f
  join likes k on k.film_id = f.id
 where id = :id;
```

</details>