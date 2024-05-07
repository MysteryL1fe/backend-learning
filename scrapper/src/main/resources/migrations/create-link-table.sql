CREATE TABLE link (
	id serial PRIMARY KEY,
	link varchar(255) UNIQUE NOT NULL,
	update_date timestamp
);