package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.dto.*;
import org.example.manager.MovieManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class MovieController {
    private MovieManager manager;

    // Получаем список всех фильмов (для всех)
    @RequestMapping("/movies.getAll")
    public List<MovieGetAllResponseDTO> getAll(int limit, long offset) {
        return manager.getAll(limit, offset);
    }

    // Получаем фильм по id (для всех)
    @RequestMapping("/movies.getById")
    public List<MovieGetByIdResponseDTO> getById(long id) {
        return manager.getById(id);
    }

    // Добавляем фильм в базу (для админа)
    @RequestMapping("/movies.create")
    public List<MovieCreateResponseDTO> create(MovieCreateRequestDTO requestDTO) {
        return manager.create(requestDTO);
    }

    // Обновляем фильм в базе (для админа)
    @RequestMapping("/movies.update")
    public List<MovieUpdateResponseDTO> update(MovieUpdateRequestDTO requestDTO) {
        return manager.update(requestDTO);
    }

    //Удаляем фильм из базы по id (для админа)
    @RequestMapping("/movies.removeById")
    public List<MovieRemoveByIdResponseDTO> removeById(long id) {
        return manager.removeById(id);
    }

    //восстанавливаем удаленный фильм по id (для админа)
    @RequestMapping("/movies.restoreById")
    public List<MovieRestoreByIdResponseDTO> restoreById(long id) {
        return manager.restoreById(id);
    }

    // просмотр всех удаленных (для админа)
    @RequestMapping("/movies.getAllRemoved")
    public List<MovieGetAllRemovedResponseDTO> getAllRemoved(int limit, long offset) {
        return manager.getAllRemoved(limit, offset);
    }

    // покупка фильма (для авторизованных пользователей)
    @RequestMapping ("movies.buy")
    public List<MovieBuyResponseDTO> buy (long id){
        return manager.buy (id);
    }

    //просмотр библиотеки купленных фильмов (для авторизованных пользователей)
    @RequestMapping ("/library.getAll")
    public List<LibraryGetAllResponseDTO> libraryGetAll (int limit, long offset) {
        return manager.libraryGetAll(limit, offset);
    }
}
