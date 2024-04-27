CREATE TABLE LINK_CHAT (
	link_id INT NOT NULL,
	chat_id INT NOT NULL,
	PRIMARY KEY (link_id, chat_id),
	FOREIGN KEY (link_id) REFERENCES link(id),
	FOREIGN KEY (chat_id) REFERENCES chat(id)
)
