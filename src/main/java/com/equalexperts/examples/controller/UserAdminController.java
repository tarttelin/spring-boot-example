package com.equalexperts.examples.controller;


import com.equalexperts.examples.dao.UserDao;
import com.equalexperts.examples.form.UserForm;
import com.equalexperts.examples.model.MyUser;
import com.equalexperts.examples.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
@RequestMapping("/admin")
public class UserAdminController {
    private final UserDao manager;
    private final PasswordEncoder encoder;

    public UserAdminController(UserDao userManager, PasswordEncoder encoder) {
        this.manager = userManager;
        this.encoder = encoder;
    }
        @RequestMapping(value = "/users", method = RequestMethod.GET)
        public String userAdmin(Model model) {
            model.addAttribute("user", new UserForm());
            return "userAdmin";
        }

        @RequestMapping(value="/users/add", method = RequestMethod.POST)
        public String add(@Valid @ModelAttribute("user") UserForm userForm, BindingResult result) {
            if (result.hasErrors()) {
                return "userAdmin";
            }
            GrantedAuthority auth = new SimpleGrantedAuthority(Role.USER.name());
            manager.createUser(new MyUser(userForm.getUsername(), encoder.encode(userForm.getPassword()), Arrays.asList(auth), userForm.getFullname(), userForm.getEmail()));
            return "redirect:/";
        }
}
