package com.example.socialnetwork;

import com.example.socialnetwork.domain.port.api.PostReactionServicePort;
import com.example.socialnetwork.domain.port.spi.PostReactionDatabasePort;
import com.example.socialnetwork.infrastructure.entity.PostReaction;
import com.example.socialnetwork.infrastructure.repository.PostReactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ThreadDemo extends Thread{
    private Thread t;
    private String threadName;
    private Long postReactionId;
    private PostReactionDatabasePort postReactionDatabasePort;


    ThreadDemo(String name, Long postReactionId, PostReactionDatabasePort  postReactionDatabasePort) {
        threadName = name;
        System.out.println("Creating " + threadName);
        this.postReactionId = postReactionId;
        this.postReactionDatabasePort = postReactionDatabasePort;
    }


    @Override
    public void run() {
        System.out.println("Running " + threadName);
        try {
            postReactionDatabasePort.updateReaction(postReactionId, "WOW");
            System.out.println("Thread " + threadName + " successes");
        } catch (Exception e) {
            System.out.println("Thread " + threadName + " interrupted: " + e.getMessage());
        }
        System.out.println("Thread " + threadName + " exiting.");
    }

    public void start() {
        System.out.println("Starting " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
}


@SpringBootTest
public class PostReactionRepositoryTest {
    @Autowired
    private PostReactionRepository postReactionRepository;

    @Autowired
    private PostReactionDatabasePort postReactionDatabasePort;

    @Test
    public void testOptimisticLock() throws InterruptedException {
        PostReaction reaction = new PostReaction();
        reaction.setReactionType("LIKE");
        postReactionRepository.saveAndFlush(reaction);

        ThreadDemo threadDemo1 = new ThreadDemo("Thread1", reaction.getId(),postReactionDatabasePort);
        ThreadDemo threadDemo2 = new ThreadDemo("Thread2", reaction.getId(),postReactionDatabasePort);
        threadDemo1.start();
        threadDemo2.start();

        // Wait for both threads to complete
        threadDemo1.join();
        threadDemo2.join();

        // Verify the final state
        PostReaction updatedReaction = postReactionRepository.findById(reaction.getId()).orElseThrow();
        assertEquals("WOW", updatedReaction.getReactionType());
    }
}
