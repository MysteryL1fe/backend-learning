package ru.khanin.dmitrii.scrapper.DTO.entity;

import java.time.OffsetDateTime;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class Link {
	protected long id;
	protected String link;
	@Nullable
	protected OffsetDateTime updateDate;
}
