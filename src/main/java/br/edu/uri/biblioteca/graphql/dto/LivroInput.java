package br.edu.uri.biblioteca.graphql.dto;

public class LivroInput {
    private String titulo;
    private String isbn;
    private Integer anoPublicacao;
    private AutorInput autor;

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Integer anoPublicacao) { this.anoPublicacao = anoPublicacao; }

    public AutorInput getAutor() { return autor; }
    public void setAutor(AutorInput autor) { this.autor = autor; }

    public static class AutorInput {
        private String nome;
        private String nacionalidade;

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        public String getNacionalidade() { return nacionalidade; }
        public void setNacionalidade(String nacionalidade) { this.nacionalidade = nacionalidade; }
    }
}
