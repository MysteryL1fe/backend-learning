CREATE TABLE IF NOT EXISTS chat (
	id serial PRIMARY KEY,
	chat_id integer UNIQUE NOT NULL,
	username varchar(255) NOT NULL
);