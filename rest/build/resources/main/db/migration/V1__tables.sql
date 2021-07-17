CREATE EXTENSION citext;
CREATE TYPE session_state AS ENUM('none', 'lobby', 'game', 'finish');
CREATE TABLE users(
    id serial PRIMARY KEY,
    nickname varchar(30) NOT NULL UNIQUE,
    email citext NOT NULL UNIQUE,
    password text NOT NULL,
    first_name text NOT NULL,
    last_name text NOT NULL,
    middle_name text,
    avatar_url text
);

CREATE TABLE sessions(
    id serial PRIMARY KEY,
    guid uuid,
    current_state session_state NOT NULL DEFAULT('none'),
    description text,
    host_id integer NOT NULL,
    players_quantity integer NOT NULL CHECK (players_quantity > 2),
    event_time timestamp  NOT NULL,
    date_time_to_choose timestamp  NOT NULL
);

CREATE TABLE gifts(
    id serial PRIMARY KEY,
    picture_url text,
    name text NOT NULL,
    description text
);

CREATE TABLE users_sessions(
    id serial PRIMARY KEY,
    user_id integer NOT NULL,
    session_id integer NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (session_id) REFERENCES sessions(id)
);

CREATE TABLE users_sessions_gifts(
    id serial PRIMARY KEY,
    user_session_id integer NOT NULL,
    gift_id integer NOT NULL,
    FOREIGN KEY (user_session_id) REFERENCES users_sessions(id),
    FOREIGN KEY (gift_id) REFERENCES  gifts(id)
);


