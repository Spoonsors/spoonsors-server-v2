package com.spoonsors.spoonsorsserver.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ToString
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //MySQL의 AUTO_INCREMENT를 사용
    @Column(nullable = false)
    private Long post_id;

    //@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "writer_id", nullable = false)
    private BMember bMember;

    @Column( length = 100)
    private String post_title;

    @Column(length = 400)
    private String post_txt;

    @ColumnDefault("0")
    private Integer post_state;

    @ColumnDefault("0") //리뷰 없으면 0, 있으면 1
    private Integer has_review;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column( nullable = false) //datetime
    private Date post_date;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.REMOVE})
    private List<Spon> spon = new ArrayList<>();

    @ColumnDefault("0")
    private Integer remain_spon;

    @Column(columnDefinition = "TEXT")
    private String menu_img;

    private String menu_name;

}
