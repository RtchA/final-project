package org.example.manager;

import lombok.AllArgsConstructor;
import org.example.authentication.Authentication;
import org.example.authenticator.Authenticator;
import org.example.dto.*;
import org.example.exception.ForbiddenException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class MovieManager {
    private NamedParameterJdbcTemplate template;
    private Authenticator authenticator;

    //получить весь список из базы
    public List<MovieGetAllResponseDTO> getAll(int limit, long offset) {
        return template.query(
                //language=PostgreSQL
                """
                        SELECT id, name, file, preview, genre, dateRelease FROM movies
                        WHERE removed = FALSE
                        ORDER BY id
                        LIMIT :limit OFFSET :offset
                        """,
                Map.of(
                        "limit", limit,
                        "offset", offset
                ),
                BeanPropertyRowMapper.newInstance(MovieGetAllResponseDTO.class)
        );
    }

    //получить по id
    public List<MovieGetByIdResponseDTO> getById(long id) {
                return template.query(
                //language=PostgreSQL
                """
                        SELECT id, name, description, preview, file, genre, dateRelease, price
                        FROM movies
                        WHERE removed=FALSE AND id=:id
                        """,
                Map.of("id", id),
                BeanPropertyRowMapper.newInstance(MovieGetByIdResponseDTO.class)
        );
    }
//добавить фильм в базу
    public List<MovieCreateResponseDTO> create(MovieCreateRequestDTO requestDTO) {
        Authentication authentication = authenticator.authenticate();
        if (authentication.isAnonymous()) {
            throw new ForbiddenException();
        }
        if (!authentication.getRole().equals(Authentication.ROLE_ADMIN)) {
            throw new ForbiddenException();
        }
        return template.query(
                //language=PostgreSQL
                """
                        INSERT INTO movies ( name, description, preview, file, genre, daterelease, price)
                        VALUES (:name, :description, :preview, :file, :genre, :daterelease, :price)
                        RETURNING id, name, description, preview, file, genre, daterelease, price
                        """,
                Map.of( "name", requestDTO.getName(),
                        "description", requestDTO.getDescription(),
                        "preview", requestDTO.getPreview(),
                        "file", requestDTO.getFile(),
                        "genre", requestDTO.getGenre(),
                        "daterelease", requestDTO.getDateRelease(),
                        "price", requestDTO.getPrice()
                ),
                BeanPropertyRowMapper.newInstance(MovieCreateResponseDTO.class)
        );

    }

    // обновить
    public List<MovieUpdateResponseDTO> update(MovieUpdateRequestDTO requestDTO) {
        Authentication authentication = authenticator.authenticate();
        if (authentication.isAnonymous()) {
            throw new ForbiddenException();
        }
        if (!authentication.getRole().equals((Authentication.ROLE_ADMIN))) {
            throw new ForbiddenException();
        }
        return template.query(
                //language=PostgreSQL
                """
                        UPDATE movies    
                        SET name= :name, description= :description, preview= :preview, file= :file, genre= :genre, daterelease= :daterelease, price= :price
                        WHERE removed=FALSE AND id= :id
                        RETURNING id, name, description, preview, file, genre, daterelease, price
                        """,
                Map.of(
                        "id", requestDTO.getId(),
                        "name", requestDTO.getName(),
                        "description", requestDTO.getDescription(),
                        "preview", requestDTO.getPreview(),
                        "file", requestDTO.getFile(),
                        "genre", requestDTO.getGenre(),
                        "daterelease", requestDTO.getDateRelease(),
                        "price", requestDTO.getPrice()
                ),
                BeanPropertyRowMapper.newInstance(MovieUpdateResponseDTO.class)
        );
    }

    // удалить по id (для админа)
    public List<MovieRemoveByIdResponseDTO> removeById(long id) {
        Authentication authentication = authenticator.authenticate();
        if (authentication.isAnonymous()) {
            throw new ForbiddenException();
        }
        if (!authentication.getRole().equals((Authentication.ROLE_ADMIN))) {
            throw new ForbiddenException();
        }
        return template.query(
                // language=PostgreSQL
                """
                        UPDATE movies SET removed=TRUE WHERE id = :id
                        RETURNING id, name, description, preview, file, genre, dateRelease
                        """,
                Map.of("id", id),
                BeanPropertyRowMapper.newInstance(MovieRemoveByIdResponseDTO.class)
        );
    }


    // восстановить удаленный (для админа)
    public List<MovieRestoreByIdResponseDTO> restoreById(long id) {
        Authentication authentication = authenticator.authenticate();
        if (authentication.isAnonymous()) {
            throw new ForbiddenException();
        }
        if (!authentication.getRole().equals((Authentication.ROLE_ADMIN))) {
            throw new ForbiddenException();
        }
        return template.query(
                // language=PostgreSQL
                """
                        UPDATE movies SET removed= FALSE
                        WHERE id = :id
                        RETURNING id, name, description, preview, file, genre, dateRelease, price
                        """,
                Map.of("id", id),
                BeanPropertyRowMapper.newInstance(MovieRestoreByIdResponseDTO.class)
        );
    }

    // просмотр списка удаленных (для админа)
    public List<MovieGetAllRemovedResponseDTO> getAllRemoved(int limit, long offset) {
        Authentication authentication = authenticator.authenticate();
        if (authentication.isAnonymous()) {
            throw new ForbiddenException();
        }
        if (!authentication.getRole().equals((Authentication.ROLE_ADMIN))) {
            throw new ForbiddenException();
        }
        return template.query(
                //language=PostgreSQL
                """
                        SELECT id, name, preview, file, genre, dateRelease FROM movies
                        WHERE removed = true 
                        ORDER BY id
                        LIMIT :limit OFFSET :offset
                        """,
                Map.of(
                        "limit", limit,
                        "offset", offset
                ),
                BeanPropertyRowMapper.newInstance(MovieGetAllRemovedResponseDTO.class)
        );
    }

    // покупка фильма по id
    public List<MovieBuyResponseDTO> buy(long id) {
        Authentication authentication = authenticator.authenticate();
        if (authentication.isAnonymous()) {
            throw new ForbiddenException();
        }
        return template.query(
                //language=PostgreSQL
                """
                        INSERT INTO library (user_id, movie_id)   
                        VALUES (:user_id, :movie_id) 
                        RETURNING id, user_id, movie_id
                        """,
                Map.of(
                        "user_id", authentication.getId(),
                        "movie_id", id

                ),
                BeanPropertyRowMapper.newInstance(MovieBuyResponseDTO.class)
        );
    }

    public List<LibraryGetAllResponseDTO> libraryGetAll(int limit, long offset) {
        Authentication authentication = authenticator.authenticate();
        long id =authentication.getId();
        if (authentication.isAnonymous()) {
            throw new ForbiddenException();
        }
        return template.query(
                //language=PostgreSQL
                """
                        SELECT l.id, l.movie_id, m.name, m.description, m.genre, m.dateRelease, m.file, m.preview FROM library l
                        JOIN movies m ON l.movie_id = m.id
                        WHERE l.user_id = :user_id
                        ORDER BY l.id
                        LIMIT :limit OFFSET :offset
                        """,
                Map.of( "user_id", authentication.getId(),
                        "limit", limit,
                        "offset", offset),
                BeanPropertyRowMapper.newInstance(LibraryGetAllResponseDTO.class)
        );
    }
}







