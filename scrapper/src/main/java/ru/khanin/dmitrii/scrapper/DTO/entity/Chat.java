package ru.khanin.dmitrii.scrapper.DTO.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class Chat {
	protected long id;
	protected long chatId;
	protected String username;
}
