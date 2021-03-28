package me.johnniang.umami.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "page_view")
@Data
public class PageView {

    @Id
    @Column(name = "view_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "url", length = 500, nullable = false)
    private String url;

    @Column(name = "referer", length = 500)
    private String referer;

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
