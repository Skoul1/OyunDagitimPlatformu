package com.odp.main.Services;

import com.odp.main.Models.Comment;
import com.odp.main.Models.Game;
import com.odp.main.Models.User;
import com.odp.main.Repositorys.CommentRepository;
import com.odp.main.Repositorys.GameRepository;
import com.odp.main.Repositorys.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    public Comment addComment(Long gameId, Long userId, String content) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setTimestamp(LocalDateTime.now());
        comment.setGame(game);
        comment.setUser(user);

        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByGameId(Long gameId) {
        return commentRepository.findByGameId(gameId);
    }
}
