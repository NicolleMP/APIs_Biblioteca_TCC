package br.edu.uri.biblioteca.exception;

public class IsbnDuplicadoException extends RuntimeException{
    public IsbnDuplicadoException(String mensagem) {
        super(mensagem);
    }
}
