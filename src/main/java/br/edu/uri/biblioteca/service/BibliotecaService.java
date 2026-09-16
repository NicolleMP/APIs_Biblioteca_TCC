package br.edu.uri.biblioteca.service;

import br.edu.uri.biblioteca.domain.Autor;
import br.edu.uri.biblioteca.domain.Livro;
import br.edu.uri.biblioteca.repository.BibliotecaRepository;
import br.edu.uri.biblioteca.exception.IsbnDuplicadoException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BibliotecaService {
    private final BibliotecaRepository repository;

    public BibliotecaService(BibliotecaRepository repository) {
        this.repository = repository;
    }

    public synchronized Livro criarLivro(String titulo, String isbn, Integer anoPublicacao, String nomeAutor, String nacionalidadeAutor) {
        // Regra de Unicidade do ISBN
        if (repository.buscarPorIsbn(isbn).isPresent()) {
            throw new IsbnDuplicadoException("ISBN já cadastrado no sistema.");
        }

        // Regra de Reutilização de Autor (Nome + Nacionalidade)
        Autor autor = repository.buscarAutorPorNomeENacionalidade(nomeAutor, nacionalidadeAutor)
                .orElseGet(() -> repository.salvarAutor(new Autor(null, nomeAutor, nacionalidadeAutor)));

        Livro livro = new Livro(null, titulo, isbn, anoPublicacao, autor);
        return repository.salvarLivro(livro);
    }

    public Livro buscarLivroPorId(Long id) {
        return repository.buscarLivroPorId(id)
                .orElseThrow(() -> new IsbnDuplicadoException("Livro não encontrado."));
    }

    public List<Livro> listarLivros() {
        return repository.listarTodosLivros();
    }

    public synchronized Livro atualizarLivro(Long id, String titulo, String isbn, Integer anoPublicacao, String nomeAutor, String nacionalidadeAutor) {
        Livro livroExistente = buscarLivroPorId(id);

        // Regra de Unicidade de ISBN na Atualização
        Optional<Livro> livroComMesmoIsbn = repository.buscarPorIsbn(isbn);
        if (livroComMesmoIsbn.isPresent() && !livroComMesmoIsbn.get().getId().equals(id)) {
            throw new IsbnDuplicadoException("ISBN já associado a outro livro.");
        }

        Autor autor = repository.buscarAutorPorNomeENacionalidade(nomeAutor, nacionalidadeAutor)
                .orElseGet(() -> repository.salvarAutor(new Autor(null, nomeAutor, nacionalidadeAutor)));

        livroExistente.setTitulo(titulo);
        livroExistente.setIsbn(isbn);
        livroExistente.setAnoPublicacao(anoPublicacao);
        livroExistente.setAutor(autor);

        return repository.salvarLivro(livroExistente);
    }

    public synchronized boolean removerLivro(Long id) {
        buscarLivroPorId(id); // Garante erro caso não exista
        return repository.deletarLivro(id);
    }
}
