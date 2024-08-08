package com.spoonsors.spoonsorsserver.entity;

import com.spoonsors.spoonsorsserver.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@ToString
@Setter
@Getter
@SuperBuilder
@Entity
@NoArgsConstructor(force = true)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //MySQL의 AUTO_INCREMENT를 사용
    @Column(name="post_id", nullable = false)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "writer_id", nullable = false)
    private BMember writer;

    @Column( name="post_title", length = 100)
    private String postTitle;

    @Column(name="post_txt", length = 400)
    private String postTxt;

    @Column(name = "post_state", columnDefinition = "TINYINT DEFAULT 0")
    private boolean postState;

    @Column(name = "has_review", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private boolean hasReview;

    @Column(name = "post_date", nullable = false)
    private LocalDate postDate;

    @Column(name = "remain_spon", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int remainSpon;

    @Column(name = "menu_img", columnDefinition = "TEXT")
    private String menuImg;

    @Column(name = "menu_name", length = 255)
    private String menuName;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.REMOVE})
    private List<Spon> spons;

}
