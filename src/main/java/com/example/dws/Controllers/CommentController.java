package com.example.dws.Controllers;

import com.example.dws.Entities.Comment;
import com.example.dws.Repositories.CommentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentController {

    private CommentRepository commentRepository;

    public CommentController(){
        this.commentRepository = new CommentRepository();
    }

    public CommentController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @GetMapping({"/contact"})
    public String contact(Model model) {
        return "contact";
    }

    @GetMapping({"/addComment"})
    public String addFormComment(Model model, @RequestParam String name, @RequestParam String mail, @RequestParam String issue, @RequestParam String message) {
        Comment comment = new Comment(name, mail, issue, message);
        this.commentRepository.putComment(comment);
        return "redirect:/contact";
    }

    @GetMapping({"/comments"})
    public String allComments(Model model) {
        model.addAttribute("comment", this.commentRepository.showComments());
        return "comments";
    }
}
