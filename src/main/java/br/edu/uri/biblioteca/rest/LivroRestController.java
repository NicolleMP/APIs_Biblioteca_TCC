package br.edu.uri.biblioteca.rest;

import br.edu.uri.biblioteca.domain.Livro;
import br.edu.uri.biblioteca.rest.dto.LivroRequestDTO;
import br.edu.uri.biblioteca.service.BibliotecaService;
import br.edu.uri.biblioteca.exception.IsbnDuplicadoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/livros")
public class LivroRestController {

    private final BibliotecaService service;

    public LivroRestController(BibliotecaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Livro> criar(@RequestBody LivroRequestDTO dto) {
        Livro criado = service.criarLivro(
                dto.getTitulo(), dto.getIsbn(), dto.getAnoPublicacao(),
                dto.getAutor().getNome(), dto.getAutor().getNacionalidade()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarLivroPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Livro>> listarTodos() {
        return ResponseEntity.ok(service.listarLivros());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizar(@PathVariable Long id, @RequestBody LivroRequestDTO dto) {
        Livro atualizado = service.atualizarLivro(
                id, dto.getTitulo(), dto.getIsbn(), dto.getAnoPublicacao(),
                dto.getAutor().getNome(), dto.getAutor().getNacionalidade()
        );
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.removerLivro(id);
        return ResponseEntity.noContent().build();
    }

    // Handlers de erro para HTTP REST
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleNotFound(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(IsbnDuplicadoException.class)
    public ResponseEntity<String> handleIsbnDuplicado(IsbnDuplicadoException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}
