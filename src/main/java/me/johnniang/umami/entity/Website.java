package me.johnniang.umami.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "website", indexes = @Index(name = "website_user_id_idx", columnList = "user_id"))
@Data
public class Website {

    @Id
    @Column(name = "website_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "website_uuid", length = 36, nullable = false, unique = true)
    private String uuid;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "domain", length = 500)
    private String domain;

    @Column(name = "share_id", unique = true)
    private String shareId;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private Account user;

}
