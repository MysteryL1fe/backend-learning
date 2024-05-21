package ru.khanin.dmitrii.scrapper.DTO.entity.jpa;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.khanin.dmitrii.scrapper.DTO.entity.Chat;

@Entity
@Table(name = "chat")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class JpaChat extends Chat {
	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "chat_seq", sequenceName = "chat_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_seq")
	protected long id;
	
	@Column(name = "chat_id")
	protected long chatId;
	
	@Column(name = "username")
	protected String username;
	
	@ManyToMany(mappedBy = "chats")
	private List<JpaLink> links;
	
	public JpaChat(Chat chat) {
		this.setId(chat.getId());
		this.setChatId(chat.getChatId());
		this.setUsername(chat.getUsername());
	}
}
