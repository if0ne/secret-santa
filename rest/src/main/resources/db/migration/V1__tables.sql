CREATE
EXTENSION citext;
CREATE TYPE session_state AS ENUM('NONE', 'LOBBY', 'GAME', 'FINISH');

CREATE TABLE users
(
    id          serial PRIMARY KEY,
    phone       varchar(15)         NOT NULL UNIQUE,
    email       varchar(254) UNIQUE NOT NULL,
    password    bytea               NOT NULL,
    first_name  text                NOT NULL,
    last_name   text                NOT NULL,
    middle_name text,
    avatar_url  text,
    telegram_id bigint unique
);

CREATE TABLE sessions
(
    id                   serial PRIMARY KEY,
    guid                 uuid,
    current_state        session_state NOT NULL DEFAULT ('NONE'),
    description          text,
    host_id              integer       NOT NULL,
    budget               integer       NOT NULL,
    min_players_quantity integer       NOT NULL CHECK (min_players_quantity > 2),
    event_timestamp      timestamp     NOT NULL,
    timestamp_to_choose  timestamp     NOT NULL
);

CREATE TABLE gifts
(
    id          serial PRIMARY KEY,
    picture_url text,
    name        text NOT NULL,
    description text
);

CREATE TABLE users_sessions
(
    id         serial PRIMARY KEY,
    user_id    integer NOT NULL,
    session_id integer NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (session_id) REFERENCES sessions (id)
);

CREATE TABLE users_sessions_gifts
(
    id              serial PRIMARY KEY,
    user_session_id integer NOT NULL,
    gift_id         integer NOT NULL,
    FOREIGN KEY (user_session_id) REFERENCES users_sessions (id),
    FOREIGN KEY (gift_id) REFERENCES gifts (id)
);

CREATE TABLE gift_giving
(
    id                     serial PRIMARY KEY,
    session_id             integer NOT NULL,
    gift_giving_user_id    integer NOT NULL,
    gift_receiving_user_id integer NOT NULL,
    FOREIGN KEY (session_id) REFERENCES sessions (id),
    FOREIGN KEY (gift_giving_user_id) REFERENCES users (id),
    FOREIGN KEY (gift_receiving_user_id) REFERENCES users (id)
);

CREATE TABLE guids
(
    telegram_guid uuid unique,
    telegram_id   bigint unique
);

