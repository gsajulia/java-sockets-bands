import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.ArrayList;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import java.io.InputStreamReader;

/**
 * A TCP server that runs on port 9090. When a client connects, it sends the
 * client the current date and time, then closes the connection with that
 * client. Arguably just about the simplest server you can write.
 */
public class BandsServer {

    /**
     * Runs the server.
     */
    public static void main(String[] args) throws IOException {
        ArrayList<Banda> base = new ArrayList<Banda>();
        ServerSocket listener = new ServerSocket(9090);
        base = readJson();

        Banda band = new Banda();
        
        try {
            // while (true) {
                Socket socket = listener.accept();
                debug("Connected");
                // try {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    

                    String userOptionFromClient, temp;


                    userOptionFromClient = in.readLine(); // read the text from client
                    debug("Read '" + userOptionFromClient + "'");
                    String[] separetedOption = userOptionFromClient.split("-");
                    debug("aaaa");
                    for (String a : separetedOption) 
                        System.out.println(a); 
                    String action = separetedOption[0];
                    String info = "";
                    if(separetedOption.length > 1){
                        info = separetedOption[1];
                    } else {
                        info = "";
                    }
                    debug("aaaa");
                    if(action.indexOf("1") != -1) {
                        // ArrayList<Musica> response = band.getMusicas();
                        //  System.out.println("A resposta é" + response); 
                        JSONObject jsonObject;
                        JSONParser jsonParser = new JSONParser();
                        jsonObject = (JSONObject) jsonParser.parse(new FileReader("src/banco.json"));
                        String response;
                        response = jsonObject.toJSONString();

                        System.out.println(response);
                        out.print(response); // send the response to client
                    } 
                    // else if (action.indexOf("2") != -1) {
                    //     boolean response = deleteBand(info);

                    //     if (response) {
                    //         out.print("Deletado com sucesso!");
                    //     } else {
                    //         out.print("Não pode ser deletado!");
                    //     }
                    // }  else if (action.indexOf("3") != -1) {
                    //     boolean response = createBand(info);

                    //     if (response) {
                    //         out.print("Criado com sucesso!");
                    //     } else {
                    //         out.print("Não pode ser criado!");
                    //     }
                    // }  else if (action.indexOf("4") != -1) {
                    //     String[] response = findBand(info);
                    //     out.print(response);
                    // }
                    
                    debug("Writing '" + userOptionFromClient + "'");

                    temp = "funcionou, string: " + userOptionFromClient; 

                    // out.print(temp); // send the response to client

                    out.flush();
                    out.close();
                    in.close();

                    socket.close();

                // } finally {
                //     socket.close();
                // }
            // }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            listener.close();
        }
    }

    private static void debug(String msg)
    {
        System.out.println("Server: " + msg);
    }

    static ArrayList<Banda> readJson() {
        ArrayList<Banda> list = new ArrayList<Banda>();
        JSONObject jsonObject;
        JSONParser jsonParser = new JSONParser();
        JSONArray songs = new JSONArray();
        JSONArray bands = new JSONArray();

        try {
            ArrayList<Musica> listsongs;

            jsonObject = (JSONObject) jsonParser.parse(new FileReader("src/banco.json"));
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

    static void writeJson(ArrayList<Banda> list) {
        Banda band;
        JSONObject obj = new JSONObject();
        JSONArray jsBands = new JSONArray();

        for(int i = 0; i < list.size(); i++){
            JSONObject jsBand = new JSONObject();
            JSONArray jsSongs = new JSONArray();
            band = list.get(i);
            jsBand.put("nome", band.getNome());
            for(int j = 0; j < band.getMusicas().size(); j++){
                JSONObject jsSong = new JSONObject();
                jsSong.put("nome", band.getMusicas().get(j).getNome());
                jsSong.put("album", band.getMusicas().get(j).getAlbum());
                jsSongs.add(jsSong);
            }
            jsBand.put("musicas", jsSongs);
            jsBands.add(jsBand);
        }

        obj.put("bandas", jsBands);
        try (FileWriter file = new FileWriter("src/banco.json")) {
            file.write(obj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}