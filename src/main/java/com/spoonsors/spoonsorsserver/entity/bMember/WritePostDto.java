package com.spoonsors.spoonsorsserver.entity.bMember;

import com.spoonsors.spoonsorsserver.entity.BMember;
import com.spoonsors.spoonsorsserver.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class WritePostDto {
    private String post_title;
    private String post_txt;
    private Date post_date;
    private BMember bMember;
    private List<String> item_list;
    private String menu_img;
    private String menu_name;

    public Post toEntity(){
        return Post.builder()
                .post_id(null)
                .post_title(post_title)
                .post_txt(post_txt)
                .post_date(post_date)
                .post_state(0)
                .has_review(0)
                .bMember(bMember)
                .remain_spon(item_list.size())
                .menu_img(menu_img)
                .menu_name(menu_name)
                .build();
    }
}
