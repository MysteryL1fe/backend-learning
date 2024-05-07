CREATE TABLE link_chat (
	link_id integer NOT NULL,
	chat_id integer NOT NULL,
	PRIMARY KEY (link_id, chat_id),
	FOREIGN KEY (link_id) REFERENCES link(id),
	FOREIGN KEY (chat_id) REFERENCES chat(id)
)
