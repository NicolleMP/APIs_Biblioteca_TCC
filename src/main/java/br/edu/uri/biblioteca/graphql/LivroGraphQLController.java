package br.edu.uri.biblioteca.graphql;

import br.edu.uri.biblioteca.domain.Livro;
import br.edu.uri.biblioteca.graphql.dto.LivroInput;
import br.edu.uri.biblioteca.service.BibliotecaService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class LivroGraphQLController {
    private final BibliotecaService service;

    public LivroGraphQLController(BibliotecaService service) {
        this.service = service;
    }

    @QueryMapping
    public Livro buscarLivroPorId(@Argument Long id) {
        return service.buscarLivroPorId(id);
    }

    @QueryMapping
    public List<Livro> listarLivros() {
        return service.listarLivros();
    }

    @MutationMapping
    public Livro criarLivro(@Argument LivroInput input) {
        return service.criarLivro(
                input.getTitulo(),
                input.getIsbn(),
                input.getAnoPublicacao(),
                input.getAutor().getNome(),
                input.getAutor().getNacionalidade()
        );
    }

    @MutationMapping
    public Livro atualizarLivro(@Argument Long id, @Argument LivroInput input) {
        return service.atualizarLivro(
                id,
                input.getTitulo(),
                input.getIsbn(),
                input.getAnoPublicacao(),
                input.getAutor().getNome(),
                input.getAutor().getNacionalidade()
        );
    }

    @MutationMapping
    public Boolean removerLivro(@Argument Long id) {
        return service.removerLivro(id);
    }
}
