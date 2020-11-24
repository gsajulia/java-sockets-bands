import java.util.ArrayList;

public class Banda {
        private String nome;
        private ArrayList<Musica> musicas;
    
        public Banda(String nome, ArrayList<Musica> musicas){
            this.nome = nome;
            this.musicas = musicas;
        }
    
        public Banda(){}
        
        public String getNome(){
            return this.nome;
        }
        public ArrayList<Musica> getMusicas(){
            return this.musicas;
        }
        
        public String toString(){
            return "nome: " + this.nome + "\n músicas: " + this.musicas.toString() + "\n";
        }
    
    
}
