package com.blue.bluearchive.user.logincontroller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserPageController {


    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/mypage")
    public String myPage(){
        return "userPage/user";
    }
    @GetMapping("/informationChange")
    public String myInformationChange(){
        return "userPage/userChange";
    }
    @GetMapping("/writingRecord")
    public String boardWirteRecord(){
        return "userPage/userBoardLog";
    }
    @GetMapping("/commentRecord")
    public String commentRecord(){
        return "userPage/userCommentLog";
    }

}
