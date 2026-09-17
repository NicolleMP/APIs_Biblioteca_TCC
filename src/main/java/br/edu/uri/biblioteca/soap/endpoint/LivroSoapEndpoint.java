package br.edu.uri.biblioteca.soap.endpoint;

import br.edu.uri.biblioteca.domain.Livro;
import br.edu.uri.biblioteca.service.BibliotecaService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import br.edu.uri.biblioteca.soap.*;

import java.util.List;

@Endpoint
public class LivroSoapEndpoint {

    private static final String NAMESPACE_URI = "http://uri.edu.br/biblioteca/soap";
    private final BibliotecaService service;

    public LivroSoapEndpoint(BibliotecaService service) {
        this.service = service;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "BuscarLivroPorIdRequest")
    @ResponsePayload
    public BuscarLivroPorIdResponse buscarLivroPorId(@RequestPayload BuscarLivroPorIdRequest request) {
        Livro livro = service.buscarLivroPorId(request.getId());
        BuscarLivroPorIdResponse response = new BuscarLivroPorIdResponse();
        response.setLivro(toLivroSoap(livro));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ListarLivrosRequest")
    @ResponsePayload
    public ListarLivrosResponse listarLivros(@RequestPayload ListarLivrosRequest request) {
        List<Livro> livros = service.listarLivros();
        ListarLivrosResponse response = new ListarLivrosResponse();
        for (Livro l : livros) {
            response.getLivros().add(toLivroSoap(l));
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CriarLivroRequest")
    @ResponsePayload
    public CriarLivroResponse criarLivro(@RequestPayload CriarLivroRequest request) {
        Livro livro = service.criarLivro(
                request.getTitulo(),
                request.getIsbn(),
                request.getAnoPublicacao(),
                request.getAutor().getNome(),
                request.getAutor().getNacionalidade()
        );
        CriarLivroResponse response = new CriarLivroResponse();
        response.setLivro(toLivroSoap(livro));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AtualizarLivroRequest")
    @ResponsePayload
    public AtualizarLivroResponse atualizarLivro(@RequestPayload AtualizarLivroRequest request) {
        Livro livro = service.atualizarLivro(
                request.getId(),
                request.getTitulo(),
                request.getIsbn(),
                request.getAnoPublicacao(),
                request.getAutor().getNome(),
                request.getAutor().getNacionalidade()
        );
        AtualizarLivroResponse response = new AtualizarLivroResponse();
        response.setLivro(toLivroSoap(livro));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "RemoverLivroRequest")
    @ResponsePayload
    public RemoverLivroResponse removerLivro(@RequestPayload RemoverLivroRequest request) {
        boolean sucesso = service.removerLivro(request.getId());
        RemoverLivroResponse response = new RemoverLivroResponse();
        response.setSucesso(sucesso);
        return response;
    }

    private LivroSoap toLivroSoap(Livro livro) {
        AutorSoap autorSoap = new AutorSoap();
        autorSoap.setId(livro.getAutor().getId());
        autorSoap.setNome(livro.getAutor().getNome());
        autorSoap.setNacionalidade(livro.getAutor().getNacionalidade());

        LivroSoap livroSoap = new LivroSoap();
        livroSoap.setId(livro.getId());
        livroSoap.setTitulo(livro.getTitulo());
        livroSoap.setIsbn(livro.getIsbn());
        livroSoap.setAnoPublicacao(livro.getAnoPublicacao());
        livroSoap.setAutor(autorSoap);
        return livroSoap;
    }
}
