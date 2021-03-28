package me.johnniang.umami.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "session", indexes = {
        @Index(name = "session_created_at_idx", columnList = "created_at"),
        @Index(name = "session_website_id_idx", columnList = "website_id")
})
@Data
public class Session {

    @Id
    @Column(name = "session_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "session_uuid", length = 36, unique = true)
    private String uuid;

    @Column(name = "hostname", length = 100)
    private String hostname;

    @Column(name = "browser", length = 20)
    private String browser;

    @Column(name = "os", length = 20)
    private String os;

    @Column(name = "device", length = 20)
    private String device;

    @Column(name = "screen", length = 11)
    private String screen;

    @Column(name = "language", length = 35)
    private String language;

    @Column(name = "country", length = 2)
    @Type(type = "character")
    private String country;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "website_id", nullable = false)
    private Website website;

}
