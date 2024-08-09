package com.spoonsors.spoonsorsserver.entity.post;

import com.spoonsors.spoonsorsserver.entity.BMember;
import com.spoonsors.spoonsorsserver.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class WritePostDto {
    private String postTitle;
    private String postTxt;
    private List<String> item_list;
    private String menuImg;
    private String menuName;

    public Post toEntity(BMember writer, int quantity) {
        return Post.builder()
                .postTitle(this.postTitle)
                .postTxt(this.postTxt)
                .isFinished(false)
                .hasReview(false)
                .remainSpon(quantity)
                .menuImg(this.menuImg)
                .menuName(this.menuName)
                .writer(writer)
                .build();
    }
}
