package me.johnniang.umami.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Event entity.
 *
 * @author johnniang
 */
@Entity
@Table(name = "event", indexes = {
        @Index(name = "event_created_at_idx", columnList = "created_at"),
        @Index(name = "event_website_id_idx", columnList = "website_id"),
        @Index(name = "event_session_id_idx", columnList = "session_id")
})
@Data
public class Event {

    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "url", length = 500, nullable = false)
    private String url;

    @Column(name = "event_type", length = 50, nullable = false)
    private String eventType;

    @Column(name = "event_value", length = 50, nullable = false)
    private String eventValue;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "website_id", nullable = false)
    private Website website;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;
}
