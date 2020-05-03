package br.com.systemasolution.myfinances.exception;

public class ErroAutenticacao extends RuntimeException {
    public ErroAutenticacao(String message) {
        super(message);
    }
}
