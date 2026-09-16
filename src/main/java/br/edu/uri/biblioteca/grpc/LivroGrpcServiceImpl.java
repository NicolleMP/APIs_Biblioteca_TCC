package br.edu.uri.biblioteca.grpc;

import br.edu.uri.biblioteca.domain.Livro;
import br.edu.uri.biblioteca.service.BibliotecaService;
import br.edu.uri.biblioteca.exception.IsbnDuplicadoException;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@GrpcService
public class LivroGrpcServiceImpl extends LivroGrpcServiceGrpc.LivroGrpcServiceImplBase {

    private final BibliotecaService service;

    public LivroGrpcServiceImpl(BibliotecaService service) {
        this.service = service;
    }

    @Override
    public void criarLivro(CriarLivroRequest request, StreamObserver<LivroMessage> responseObserver) {
        try {
            Livro livro = service.criarLivro(
                    request.getTitulo(),
                    request.getIsbn(),
                    request.getAnoPublicacao(),
                    request.getAutor().getNome(),
                    request.getAutor().getNacionalidade()
            );
            responseObserver.onNext(toLivroMessage(livro));
            responseObserver.onCompleted();
        } catch (IsbnDuplicadoException e) {
            responseObserver.onError(Status.ALREADY_EXISTS.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void buscarLivroPorId(BuscarLivroPorIdRequest request, StreamObserver<LivroMessage> responseObserver) {
        try {
            Livro livro = service.buscarLivroPorId(request.getId());
            responseObserver.onNext(toLivroMessage(livro));
            responseObserver.onCompleted();
        } catch (NoSuchElementException e) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void listarLivros(ListarLivrosRequest request, StreamObserver<ListarLivrosResponse> responseObserver) {
        List<Livro> livros = service.listarLivros();
        List<LivroMessage> messages = livros.stream()
                .map(this::toLivroMessage)
                .collect(Collectors.toList());

        ListarLivrosResponse response = ListarLivrosResponse.newBuilder()
                .addAllLivros(messages)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void atualizarLivro(AtualizarLivroRequest request, StreamObserver<LivroMessage> responseObserver) {
        try {
            Livro atualizado = service.atualizarLivro(
                    request.getId(),
                    request.getTitulo(),
                    request.getIsbn(),
                    request.getAnoPublicacao(),
                    request.getAutor().getNome(),
                    request.getAutor().getNacionalidade()
            );
            responseObserver.onNext(toLivroMessage(atualizado));
            responseObserver.onCompleted();
        } catch (NoSuchElementException e) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        } catch (IsbnDuplicadoException e) {
            responseObserver.onError(Status.ALREADY_EXISTS.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void removerLivro(RemoverLivroRequest request, StreamObserver<RemoverLivroResponse> responseObserver) {
        try {
            boolean sucesso = service.removerLivro(request.getId());
            RemoverLivroResponse response = RemoverLivroResponse.newBuilder().setSucesso(sucesso).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (NoSuchElementException e) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    // Método auxiliar para conversão da entidade de domínio para a mensagem Protobuf
    private LivroMessage toLivroMessage(Livro livro) {
        AutorMessage autorMsg = AutorMessage.newBuilder()
                .setId(livro.getAutor().getId())
                .setNome(livro.getAutor().getNome())
                .setNacionalidade(livro.getAutor().getNacionalidade())
                .build();

        return LivroMessage.newBuilder()
                .setId(livro.getId())
                .setTitulo(livro.getTitulo())
                .setIsbn(livro.getIsbn())
                .setAnoPublicacao(livro.getAnoPublicacao())
                .setAutor(autorMsg)
                .build();
    }
}