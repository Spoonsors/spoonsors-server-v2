package com.spoonsors.spoonsorsserver.entity.bMember;

import com.spoonsors.spoonsorsserver.entity.BMember;
import com.spoonsors.spoonsorsserver.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class WritePostDto {
    private String post_title;
    private String post_txt;
    private LocalDate post_date;
    private BMember bMember;
    private List<String> item_list;
    private String menu_img;
    private String menu_name;

    public Post toEntity(){
        return Post.builder()
                .postId(null)
                .postTitle(post_title)
                .postTxt(post_txt)
                .postDate(post_date)
                .postState(false)
                .hasReview(false)
                .writer(bMember)
                .remainSpon(item_list.size())
                .menuImg(menu_img)
                .menuName(menu_name)
                .build();
    }
}
