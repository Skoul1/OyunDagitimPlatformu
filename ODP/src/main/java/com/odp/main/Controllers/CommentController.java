package com.odp.main.Controllers;

import com.odp.main.Models.Comment;
import com.odp.main.Services.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/add")
    public Comment addComment(@RequestParam Long gameId, @RequestParam Long userId, @RequestParam String content) {
        return commentService.addComment(gameId, userId, content);
    }

    @GetMapping("/game/{gameId}")
    public List<Comment> getCommentsByGameId(@PathVariable Long gameId) {
        return commentService.getCommentsByGameId(gameId);
    }
}
