package me.johnniang.umami.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pageview", indexes = {
        @Index(name = "pageview_created_at_idx", columnList = "created_at"),
        @Index(name = "pageview_website_id_idx", columnList = "website_id"),
        @Index(name = "pageview_session_id_idx", columnList = "session_id"),
        @Index(name = "pageview_website_id_created_at_idx", columnList = "website_id, created_at"),
        @Index(name = "pageview_website_id_session_id_created_at_idx", columnList = "website_id, session_id, created_at")
})
@Data
public class PageView {

    @Id
    @Column(name = "view_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "url", length = 500, nullable = false)
    private String url;

    @Column(name = "referrer", length = 500)
    private String referrer;

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
