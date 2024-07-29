package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.common.mapper.PostReactionMapper;
import com.example.socialnetwork.domain.model.PostReactionDomain;
import com.example.socialnetwork.domain.port.spi.PostReactionDatabasePort;
import com.example.socialnetwork.infrastructure.entity.PostReaction;
import com.example.socialnetwork.infrastructure.repository.PostReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import static com.example.socialnetwork.infrastructure.specification.PostReactionSpecification.withUserIdAndVisibility;


@RequiredArgsConstructor
public class PostReactionDatabaseAdapter implements PostReactionDatabasePort {

    private final PostReactionRepository postReactionRepository;

    @Override
    public PostReactionDomain createPostReaction(PostReactionDomain postReactionDomain) {
        PostReaction postReaction = PostReactionMapper.INSTANCE.domainToEntity(postReactionDomain);
        return PostReactionMapper.INSTANCE.entityToDomain(postReactionRepository.save(postReaction));
    }

    @Override
    public Boolean deletePostReaction(Long postReactionId) {
        if(postReactionRepository.existsById(postReactionId)) {
            postReactionRepository.deleteById(postReactionId);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public PostReactionDomain getPostReaction(Long postReactionId) {
        PostReaction postReaction = postReactionRepository.findById(postReactionId).orElse(null);
        return postReaction != null ? PostReactionMapper.INSTANCE.entityToDomain(postReaction) : null;
    }

    @Override
    public Page<PostReactionDomain> getAllPostReactions(int page, int pageSize, Sort sort, Long postId, String postReactionType) {
        var pageable = PageRequest.of(page - 1, pageSize, sort);
        var spec = getSpec(postId,postReactionType);
        return postReactionRepository.findAll(spec, pageable).map(PostReactionMapper.INSTANCE::entityToDomain);
    }

    private Specification<PostReaction> getSpec(Long postId, String postReactionType) {
        Specification<PostReaction> spec = Specification.where(null);
        if (postId != null) {
            spec = spec.and(withUserIdAndVisibility(postId, postReactionType));
        }
        return spec;
    }
}
