package org.example.manager;

import lombok.AllArgsConstructor;
import org.example.authentication.Authentication;
import org.example.authenticator.Authenticator;
import org.example.dto.*;
import org.example.exception.ForbiddenException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class UserManager {
    private NamedParameterJdbcTemplate template;
    private Authenticator authenticator;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<UserGetAllResponseDTO> getAll(int limit, long offset) {
        Authentication authentication = authenticator.authenticate();
        if (!authentication.getRole().equals((Authentication.ROLE_ADMIN))) {
            throw new ForbiddenException();
        }
        return template.query(
                //language=PostgreSQL
                """
                select id, login, role from users
                where removed=false
                order by id
                limit :limit offset :offset
                """,
                Map.of(
                        "limit", limit,
                        "offset", offset
                ),
                BeanPropertyRowMapper.newInstance(UserGetAllResponseDTO.class)
        );
        }

    public UserRegisterResponseDTO register(UserRegisterRequestDTO requestDTO) {
        String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());
        return template.queryForObject(
                //language=PostgreSQL
                """
                insert into users (login, password, role) 
                values (:login,:password,:role)
                RETURNING id, login, role
                """,
                Map.of(
                        "login", requestDTO.getLogin(),
                        "password", encodedPassword,
                        "role", Authentication.ROLE_USER
                ),
                BeanPropertyRowMapper.newInstance(UserRegisterResponseDTO.class)
        );
    }

    public UserMeResponseDTO me (){
        Authentication authentication = authenticator.authenticate();
        return new UserMeResponseDTO(
                authentication.getId(),
                authentication.getLogin(),
                authentication.getRole()
        );
    }

    public UserRemoveByIdResponseDTO removeByID(long id) {
        Authentication authentication = authenticator.authenticate();
        if (authentication.getRole().equals(Authentication.ROLE_ADMIN)){
        return template.queryForObject(
                //language=PostgreSQL
                """
                update users set removed= true where id =:id
                returning id, login,role
                """,
                Map.of("id", id),
                BeanPropertyRowMapper.newInstance(UserRemoveByIdResponseDTO.class)
        );
    }
        if (authentication.getId()==id) {
            return template.queryForObject(
                    //language=PostgreSQL
                    """
                    update users set removed= true where id =:id
                    returning id, login, role
                    """,
                    Map.of("id", id),
                    BeanPropertyRowMapper.newInstance(UserRemoveByIdResponseDTO.class)
            );
        }
        throw new ForbiddenException();
}

    public UserRestoreByIdResponseDTO restoreById(long id) {
        Authentication authentication = authenticator.authenticate();
        if (!authentication.getRole().equals(Authentication.ROLE_ADMIN)){
            throw new ForbiddenException();
        }
        return template.queryForObject(
                //language=PostgreSQL
                """
                update users set removed = false where id = :id
                returning id, login, role
                """,
                Map.of("id", id),
                BeanPropertyRowMapper.newInstance(UserRestoreByIdResponseDTO.class)
        );
    }
}