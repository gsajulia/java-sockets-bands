import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JOptionPane;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import java.util.ArrayList;
import java.io.FileWriter;
import org.json.simple.*;
// import com.sun.imageio.plugins.common.InputStreamAdapter;

public class BandsClient {
    public static void main(String[] args) throws IOException {
        // String serverAddress = JOptionPane.showInputDialog(
        //                      "Enter IP Address of a machine that is\n" +
        //                      "running the date service on port 9090:");


        // Socket s = new Socket(serverAddress, 9090);
        Socket s = null;
        PrintWriter out = null;
        BufferedReader in = null;
        BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
        try
        {
        s = new Socket("localhost", 9090);
        debug("Connected\n");
        System.out.println("Opcoes - Inserir o número desejado, o separador - e a string que deseja utilizar depois do separador\n\n Listar todos (1)\n Excluir (2-[banda])\n Adicionar Banda (3-[nome da banda]) \n Buscar (4-[nome da banda])");

        out = new PrintWriter(s.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        
        String userOptionToServer, temp;
        ArrayList<Banda> list = null;

        userOptionToServer = read.readLine();
        debug("Sending '" + userOptionToServer + "'");
        out.print(userOptionToServer + "\r\n"); // send to server
        out.flush();

        String serverResponse = null;
        while ((serverResponse = in.readLine()) != null)
            list = convertToListJson(serverResponse);
        // debug(serverResponse);
        
        debug("Lista de músicas: " + list);
        // list.forEach(System.out::println);

        out.close();
        in.close();
        read.close();
        s.close();

    }  catch (IOException e)
    {
        e.printStackTrace();
    }
    }

    private static void debug(String msg)
    {
        System.out.println("Client: " + msg);
    }

    static ArrayList<Banda> convertToListJson(String response) {
        ArrayList<Banda> list = new ArrayList<Banda>();
        JSONObject jsonObject;
        JSONParser jsonParser = new JSONParser();
        JSONArray songs = new JSONArray();
        JSONArray bands = new JSONArray();

        try {
            ArrayList<Musica> listsongs;
            jsonObject = (JSONObject) jsonParser.parse(response);

            bands = (JSONArray) jsonObject.get("bandas");
            for (int i = 0; i < bands.size(); i++) {
                listsongs = new ArrayList<Musica>();
                JSONObject band = (JSONObject) bands.get(i);
                songs = (JSONArray) band.get("musicas");
                for (int j = 0; j < songs.size(); j++) {
                    JSONObject song = (JSONObject) songs.get(j);
                    listsongs.add(new Musica((String) song.get("nome"), (String) song.get("album")));
                }
                list.add(new Banda((String) band.get("nome"), listsongs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}