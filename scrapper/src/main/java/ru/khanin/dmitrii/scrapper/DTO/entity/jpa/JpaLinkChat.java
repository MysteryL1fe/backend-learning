package ru.khanin.dmitrii.scrapper.DTO.entity.jpa;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.khanin.dmitrii.scrapper.DTO.entity.LinkChat;

@Entity
@Table(name = "link_chat")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Accessors(chain = true)
public class JpaLinkChat extends LinkChat {
	@EmbeddedId
	private LinkChatId linkChatId;
	
	@Override
	public long getLinkId() {
		return linkChatId.linkId;
	}
	
	@Override
	public void setLinkId(long linkId) {
		if (this.linkChatId == null) this.linkChatId = new LinkChatId();
		linkChatId.linkId = linkId;
	}
	
	@Override
	public long getChatId() {
		return linkChatId.chatId;
	}
	
	@Override
	public void setChatId(long chatId) {
		if (this.linkChatId == null) this.linkChatId = new LinkChatId();
		linkChatId.chatId = chatId;
	}
	
	public JpaLinkChat(LinkChat linkChat) {
		this.linkChatId = new LinkChatId();
		this.linkChatId.linkId = linkChat.getLinkId();
		this.linkChatId.chatId = linkChat.getChatId();
	}
	
	@Embeddable
	public static class LinkChatId implements Serializable {
		@Column(name = "link_id")
		private long linkId;
		
		@Column(name = "chat_id")
		private long chatId;

		public long getLinkId() {
			return linkId;
		}

		public void setLinkId(long linkId) {
			this.linkId = linkId;
		}

		public long getChatId() {
			return chatId;
		}

		public void setChatId(long chatId) {
			this.chatId = chatId;
		}		
	}
}
