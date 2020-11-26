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
                    

                    String userOptionFromClient, temp, data;
                    Long option;
                    Integer optionInt;
                    boolean status;

                    userOptionFromClient = in.readLine(); // read the text from client
                    JSONObject jsonObject;
                    JSONParser jsonParser = new JSONParser();
                    jsonObject = (JSONObject) jsonParser.parse(userOptionFromClient);
                    data = (String) jsonObject.get("data");
                    option = (Long) jsonObject.get("options");

                    System.out.println("A resposta é" + data + option); 
                    
                    optionInt = Integer.valueOf(option.toString());

                    System.out.println(optionInt); 

                    switch(optionInt) {
                        case 1:                      
                            jsonObject = (JSONObject) jsonParser.parse(new FileReader("src/banco.json"));
                            String response;
                            response = jsonObject.toJSONString();
    
                            System.out.println(response);
                            out.print(response); // send the response to client
                            break;
                        case 2:
                            status = deleteBand(data);

                            if (status) {
                                out.print("Deletado com sucesso!");
                            } else {
                                out.print("Não pode ser deletado!");
                            }
                            break;
                        case 3:
                            status = createBand(data);

                            if (status) {
                                out.print("Criado com sucesso!");
                            } else {
                                out.print("Não pode ser criado!");
                            }
                            break;
                        case 4:
                            String[] searchResponse = findBand(data);
                            out.print(searchResponse);
                            break;
                        default:
                            out.print("Essa opção não foi encontrada.");
                            break;

                    }
                    
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

    private static boolean deleteBand(String info)
    {
        System.out.println("Deletando bandas com nome de" + info);
        return true;
    }

    private static boolean createBand(String info)
    {
        System.out.println("Criando bandas com nome de" + info);
        return true;
    }

    private static String[] findBand(String info)
    {
        System.out.println("Buscando bandas com nome de" + info);
        String[] response = {"a"};
        return response; 
    }
}