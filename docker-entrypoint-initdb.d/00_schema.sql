CREATE TABLE users
(
    id       bigserial primary key,
    login    text        not null unique,
    password text        not null,
    role     text        not null,
    removed  boolean     not null default false,
    created  timestamptz not null default current_timestamp
);

CREATE TABLE movies
(
    id bigserial primary key,
    name text not null,
    description text not null,
    preview text not null,
    file text not null,
    genre text,
    dateRelease bigint,
    price bigint,
    removed boolean not null default false,
    created timestamptz not null default current_timestamp
);

CREATE TABLE library
(
    id bigserial primary key,
    user_id bigint references users not null,
    movie_id bigint references movies not null
);