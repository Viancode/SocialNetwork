package com.example.socialnetwork.domain.schedule;

import com.example.socialnetwork.domain.model.CommentDomain;
import com.example.socialnetwork.domain.port.spi.CommentDatabasePort;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.LoadingModelEvaluatorBuilder;
import org.jpmml.evaluator.ModelEvaluator;
import org.jpmml.evaluator.ModelEvaluatorFactory;
import org.jpmml.model.PMMLUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.pmml4s.model.Model;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class HideCommentSchedule {
    private final CommentDatabasePort commentDatabasePort;
    private Model model;
    private static final double SPAM_THRESHOLD = 0.6;

    @PostConstruct
    public void init()  {
        try {
            model = Model.fromFile("/mnt/ntfs/Workspace/Project/SocialNetwork/src/main/resources/model/comment-model.pmml");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load model");
        }
    }

//    @Scheduled(fixedRate = 24, timeUnit = TimeUnit.HOURS)
    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    public void hideComment() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        List<CommentDomain> comments = commentDatabasePort.findAllUpdateWithinLastDay(yesterday);
        for (CommentDomain comment : comments) {
            if (isSpam(comment)) {
                comment.setIsHidden(true);
                commentDatabasePort.updateComment(comment);
            }
        }
    }

    private boolean isSpam(CommentDomain comment) {
        Map<String, Object> input = new HashMap<>();
        input.put("free_text", comment.getContent());

        Map<?, ?> results = model.predict(input);
        double spamProbability = (double) results.get("probability(1)");
        System.out.println(comment.getContent() + " " + spamProbability);
        return spamProbability > SPAM_THRESHOLD;
    }
}
