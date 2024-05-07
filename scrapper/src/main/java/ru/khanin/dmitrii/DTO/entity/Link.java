package ru.khanin.dmitrii.DTO.entity;

import java.time.OffsetDateTime;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class Link {
	private Long id;
	private String link;
	@Nullable
	private OffsetDateTime updateDate;
}
