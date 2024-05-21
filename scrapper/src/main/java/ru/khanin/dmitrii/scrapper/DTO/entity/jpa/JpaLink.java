package ru.khanin.dmitrii.scrapper.DTO.entity.jpa;

import java.time.OffsetDateTime;
import java.util.List;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.khanin.dmitrii.scrapper.DTO.entity.Link;

@Entity
@Table(name = "link")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class JpaLink extends Link {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	protected long id;
	
	@Column(name = "link")
	protected String link;
	
	@Column(name = "update_date")
	@Nullable
	protected OffsetDateTime updateDate;
	
	@ManyToMany
	@JoinTable(
			name = "link_chat",
			joinColumns = @JoinColumn(name = "link_id"),
			inverseJoinColumns = @JoinColumn(name = "chat_id")
	)
	private List<JpaChat> chats;
	
	public JpaLink(Link link) {
		this.setId(link.getId());
		this.setLink(link.getLink());
		this.setUpdateDate(link.getUpdateDate());
	}
}
