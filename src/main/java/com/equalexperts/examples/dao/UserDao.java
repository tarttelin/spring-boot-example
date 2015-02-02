package com.equalexperts.examples.dao;

import com.equalexperts.examples.model.MyUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserDao implements UserDetailsService {
    private final JdbcTemplate template;

    public UserDao(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<MyUser> users = template.query("select username, password, enabled, email, fullname from users where username = ?", this::mapUser, username);

        if (users.isEmpty()) {
            throw new UsernameNotFoundException(String.format("Username %s not found", username));
        }

        MyUser user = users.get(0); // contains no GrantedAuthority[]

        Set<GrantedAuthority> dbAuthsSet = new HashSet<GrantedAuthority>(loadUserAuthorities(user.getUsername()));
        List<GrantedAuthority> dbAuths = new ArrayList<GrantedAuthority>(dbAuthsSet);

        if (dbAuths.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User %s has no GrantedAuthority", username));
        }

        return new MyUser(username, user.getPassword(), dbAuths,user.getFullname(), user.getEmail(), user.isAccountNonExpired(), user.isAccountNonLocked(), user.isCredentialsNonExpired(), user.isEnabled());
    }

    public void createUser(MyUser user) {
        validateUserDetails(user);
        template.update("insert into users (username, password, enabled, email, fullname) values (?, ?, ?, ?, ?)",
                user.getUsername(), user.getPassword(), user.isEnabled(), user.getEmail(), user.getFullname());

        insertUserAuthorities(user);

    }


    protected List<GrantedAuthority> loadUserAuthorities(String username) {
        return template.query("select username, authority from authorities where username = ?", this::mapAuthority, username);
    }

    private void insertUserAuthorities(MyUser user) {
        user.getAuthorities().stream().forEach(grantedAuthority ->
                template.update(
                        "insert into authorities (username, authority) values (?,?)",
                        user.getUsername(), grantedAuthority.getAuthority()));
    }

    private void validateUserDetails(UserDetails user) {
        Assert.hasText(user.getUsername(), "Username may not be empty or null");
        validateAuthorities(user.getAuthorities());
    }

    private void validateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Authorities list must not be null");

        for (GrantedAuthority authority : authorities) {
            Assert.notNull(authority, "Authorities list contains a null entry");
            Assert.hasText(authority.getAuthority(), "getAuthority() method must return a non-empty string");
        }
    }

    // # ROW MAPPERS
    private MyUser mapUser(ResultSet rs, int row) throws SQLException {
        return new MyUser(rs.getString("username"), rs.getString("password"), AuthorityUtils.NO_AUTHORITIES, rs.getString("fullname"), rs.getString("email"), true, true, true, rs.getBoolean("enabled"));
    }

    private SimpleGrantedAuthority mapAuthority(ResultSet rs, int row) throws SQLException {
        return new SimpleGrantedAuthority(rs.getString("authority"));
    }

}
