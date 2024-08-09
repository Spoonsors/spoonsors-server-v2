package com.spoonsors.spoonsorsserver.repository;

import com.spoonsors.spoonsorsserver.entity.BMember;
import com.spoonsors.spoonsorsserver.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPostRepository extends JpaRepository<Post, Long>  {
    List<Post> findByDeletedYnFalseOrderByCreatedAtDesc();

    List<Post> findByWriterAndDeletedYnFalseOrderByCreatedAtDesc(BMember writer);

}
