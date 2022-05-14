package org.example.authenticator;

import lombok.AllArgsConstructor;
import org.example.authentication.Authentication;
import org.example.exception.NotAuthenticatedException;
import org.example.exception.PasswordNotMatchesException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@AllArgsConstructor

public class Authenticator {
    private HttpServletRequest request;
    private JdbcTemplate template;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Authentication authenticate (){
        String login = request.getHeader("X-login");
        if (login == null) {
            return anonymous();
        }
        String password = request.getHeader("X-password");
        if (password == null) {
            throw new NotAuthenticatedException();
        }
        Authentication authentication = template.queryForObject(
                //language=PostgreSQL
                """
                        select id, login, password, role from users
                        where login = ? and removed=false
                        """,
                new Object[]{login},
                BeanPropertyRowMapper.newInstance(Authentication.class)
        );

        if (!passwordEncoder.matches(password, authentication.getPassword())){
            throw new PasswordNotMatchesException();
        }

        authentication.setPassword("***");
        return authentication;
        }

//ctrl+alt+c для назначения роли, поставить галочку move to another class

    private Authentication anonymous (){
       return new Authentication(
               Authentication.ID_ANONYMOUS,
               "anonymous",
               "***",
               Authentication.ROLE_ANONYMOUS);
    }
}
