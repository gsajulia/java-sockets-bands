public class Musica {
    private String nome;
    private String album;

    public Musica(String nome, String album){
        this.nome = nome;
        this.album = album;
    }

    public Musica(){}
    
    public String getNome(){
        return this.nome;
    }
    public String getAlbum(){
        return this.album;
    }

    public String toString(){
        return "\n Nome: " + this.nome + "\n album: " + this.album;
    }
}
