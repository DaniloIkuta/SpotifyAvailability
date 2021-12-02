package br.daniloikuta.spotifyavailability.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import br.daniloikuta.spotifyavailability.configuration.AuditRevisionListener;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "revision_info")
@RevisionEntity(AuditRevisionListener.class)
@AttributeOverride(name = "timestamp", column = @Column(name = "revision_timestamp"))
@AttributeOverride(name = "id", column = @Column(name = "revision_id"))
@NoArgsConstructor
@AllArgsConstructor
public class AuditRevisionEntity extends DefaultRevisionEntity {
	private static final long serialVersionUID = -7470031086837182814L;

	@Column(name = "user")
	@Setter
	private String user;
}
