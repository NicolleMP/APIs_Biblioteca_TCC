package br.edu.uri.biblioteca.repository;

import br.edu.uri.biblioteca.domain.Autor;
import br.edu.uri.biblioteca.domain.Livro;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class BibliotecaRepository {
    private final Map<Long, Livro> livrosMap = new ConcurrentHashMap<>();
    private final Map<Long, Autor> autoresMap = new ConcurrentHashMap<>();

    private final AtomicLong livroIdSequence = new AtomicLong(0);
    private final AtomicLong autorIdSequence = new AtomicLong(0);

    public Long gerarLivroId() { return livroIdSequence.incrementAndGet(); }
    public Long gerarAutorId() { return autorIdSequence.incrementAndGet(); }

    public void setLivroIdSequence(long val) { livroIdSequence.set(val); }
    public void setAutorIdSequence(long val) { autorIdSequence.set(val); }

    public Livro salvarLivro(Livro livro) {
        if (livro.getId() == null) {
            livro.setId(gerarLivroId());
        }
        livrosMap.put(livro.getId(), livro);
        return livro;
    }

    public Optional<Livro> buscarLivroPorId(Long id) {
        return Optional.ofNullable(livrosMap.get(id));
    }

    public List<Livro> listarTodosLivros() {
        return new ArrayList<>(livrosMap.values());
    }

    public boolean deletarLivro(Long id) {
        return livrosMap.remove(id) != null;
    }

    public Optional<Livro> buscarPorIsbn(String isbn) {
        return livrosMap.values().stream()
                .filter(l -> l.getIsbn().equalsIgnoreCase(isbn))
                .findFirst();
    }

    public Autor salvarAutor(Autor autor) {
        if (autor.getId() == null) {
            autor.setId(gerarAutorId());
        }
        autoresMap.put(autor.getId(), autor);
        return autor;
    }

    public Optional<Autor> buscarAutorPorNomeENacionalidade(String nome, String nacionalidade) {
        return autoresMap.values().stream()
                .filter(a -> a.getNome().equalsIgnoreCase(nome) &&
                        a.getNacionalidade().equalsIgnoreCase(nacionalidade))
                .findFirst();
    }

}