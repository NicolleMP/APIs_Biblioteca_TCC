package br.edu.uri.biblioteca.service;

import br.edu.uri.biblioteca.domain.Autor;
import br.edu.uri.biblioteca.domain.Livro;
import br.edu.uri.biblioteca.exception.IsbnDuplicadoException;
import br.edu.uri.biblioteca.repository.BibliotecaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BibliotecaService {

    private final BibliotecaRepository repository;

    public BibliotecaService(BibliotecaRepository repository) {
        this.repository = repository;
    }

    public synchronized Livro criarLivro(
            String titulo,
            String isbn,
            Integer anoPublicacao,
            String nomeAutor,
            String nacionalidadeAutor) {

        // Regra de unicidade do ISBN
        if (repository.buscarPorIsbn(isbn).isPresent()) {
            throw new IsbnDuplicadoException(
                    "ISBN já cadastrado no sistema."
            );
        }

        // Regra de reutilização de autor
        Autor autor = repository
                .buscarAutorPorNomeENacionalidade(
                        nomeAutor,
                        nacionalidadeAutor
                )
                .orElseGet(() ->
                        repository.salvarAutor(
                                new Autor(
                                        null,
                                        nomeAutor,
                                        nacionalidadeAutor
                                )
                        )
                );

        Livro livro = new Livro(
                null,
                titulo,
                isbn,
                anoPublicacao,
                autor
        );

        return repository.salvarLivro(livro);
    }

    public Livro buscarLivroPorId(Long id) {
        return repository.buscarLivroPorId(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Livro não encontrado."
                        )
                );
    }

    public List<Livro> listarLivros() {
        return repository.listarTodosLivros();
    }

    public synchronized Livro atualizarLivro(
            Long id,
            String titulo,
            String isbn,
            Integer anoPublicacao,
            String nomeAutor,
            String nacionalidadeAutor) {

        Livro livroExistente = buscarLivroPorId(id);

        // Regra de unicidade do ISBN na atualização
        Optional<Livro> livroComMesmoIsbn =
                repository.buscarPorIsbn(isbn);

        if (livroComMesmoIsbn.isPresent()
                && !livroComMesmoIsbn.get().getId().equals(id)) {

            throw new IsbnDuplicadoException(
                    "ISBN já associado a outro livro."
            );
        }

        // Regra de reutilização de autor
        Autor autor = repository
                .buscarAutorPorNomeENacionalidade(
                        nomeAutor,
                        nacionalidadeAutor
                )
                .orElseGet(() ->
                        repository.salvarAutor(
                                new Autor(
                                        null,
                                        nomeAutor,
                                        nacionalidadeAutor
                                )
                        )
                );

        livroExistente.setTitulo(titulo);
        livroExistente.setIsbn(isbn);
        livroExistente.setAnoPublicacao(anoPublicacao);
        livroExistente.setAutor(autor);

        return repository.salvarLivro(livroExistente);
    }

    public synchronized boolean removerLivro(Long id) {
        buscarLivroPorId(id);
        return repository.deletarLivro(id);
    }
}