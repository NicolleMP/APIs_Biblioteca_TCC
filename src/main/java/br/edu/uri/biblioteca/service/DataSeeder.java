package br.edu.uri.biblioteca.service;

import br.edu.uri.biblioteca.domain.Autor;
import br.edu.uri.biblioteca.domain.Livro;
import br.edu.uri.biblioteca.repository.BibliotecaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {
    private final BibliotecaRepository repository;

    public DataSeeder(BibliotecaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Autor> autores = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Autor autor = new Autor((long) i, "Autor " + i, "Nacionalidade " + i);
            autores.add(repository.salvarAutor(autor));
        }
        repository.setAutorIdSequence(10);

        for (int i = 1; i <= 100; i++) {
            Autor autorDoLivro = autores.get((i - 1) % 10);
            Livro livro = new Livro(
                    (long) i,
                    "Livro Título " + i,
                    "978-00000000" + String.format("%02d", i),
                    2000 + (i % 24),
                    autorDoLivro
            );
            repository.salvarLivro(livro);
        }
        repository.setLivroIdSequence(100);
        System.out.println("Seeding concluído: 10 autores e 100 livros criados em memória!");
    }
}
