package org.example.advice;

import org.example.dto.ExceptionDTO;
import org.example.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice

public class ExceptionControllerAdvice {
    @ResponseStatus (HttpStatus.FORBIDDEN)
    @ExceptionHandler
public ExceptionDTO handle (ForbiddenException e){
        e.printStackTrace();
        return new ExceptionDTO ("Доступ запрещен");
    }

 @ResponseStatus (HttpStatus.UNAUTHORIZED)
    @ExceptionHandler
    public ExceptionDTO handle (NotAuthenticatedException e){
        e.printStackTrace ();
        return new ExceptionDTO("Пользователь не авторизован");

 }

 @ResponseStatus (HttpStatus.UNAUTHORIZED)
    @ExceptionHandler
    public ExceptionDTO handle (PasswordNotMatchesException e){
        e.printStackTrace();
        return new ExceptionDTO("Неверный пароль");
 }

 @ResponseStatus (HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ExceptionDTO handle (UnsupportedContentTypeException e){
        e.printStackTrace();
        return  new ExceptionDTO("Неподдерживаемый формат файла");
 }

 @ResponseStatus (HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ExceptionDTO handle  (UploadException e){
        e.printStackTrace();
        return new ExceptionDTO("Ошибка загрузки");
 }
}
