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

public class BandsClient {
    public static void main(String[] args) throws IOException {
        // String serverAddress = JOptionPane.showInputDialog(
        // "Enter IP Address of a machine that is\n" +
        // "running the date service on port 9090:");

        Socket s = null;
        PrintWriter out = null;
        BufferedReader in = null;
        BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
        try {
            s = new Socket("localhost", 9090);
            debug("Connected\n");

            int option;
            String userOptionToServer;
            Scanner scanner = new Scanner(System.in);
            ArrayList<Banda> list = null;

            System.out.println("Opcoes\n\n 1 - Listar todos\n 2 - Excluir \n 3 - Adicionar Banda \n 4 - Buscar");
            option = scanner.nextInt();

            if (option == 2) {
                System.out.println("Digite o nome da banda que deseja excluir:");
            } else if (option == 3) {
                System.out.println("Digite o nome da banda que deseja adicionar:");
            } else if (option == 4) {
                System.out.println("Digite o nome da banda que deseja buscar:");
            }

            out = new PrintWriter(s.getOutputStream(), true); //send
            in = new BufferedReader(new InputStreamReader(s.getInputStream())); //receive

            if (option != 1) {
                userOptionToServer = read.readLine();
            } else {
                userOptionToServer = "";
            }

            JSONObject jsOptions = new JSONObject(); //options that will be sent to the server
            jsOptions.put("options", option);
            jsOptions.put("data", userOptionToServer);
            String response;
            response = jsOptions.toJSONString();

            debug("Mostrando options: " + jsOptions);
            debug("Sending '" + userOptionToServer + "'");
            out.print(response + "\r\n"); // send to server
            out.flush();
            String serverResponse = null;

            // Showing the answer received from the server
            switch (option) {
                case 1:
                    while ((serverResponse = in.readLine()) != null)
                        list = convertToListJson(serverResponse);

                    debug("Lista de músicas: ");
                    list.forEach(System.out::println);

                    break;
                case 2:
                    while ((serverResponse = in.readLine()) != null)
                        debug(serverResponse);
                    break;
                case 3:
                    while ((serverResponse = in.readLine()) != null)
                        debug(serverResponse);
                    break;
                case 4:
                    Banda band = new Banda();
                    while ((serverResponse = in.readLine()) != null) {
                        if (!serverResponse.equals("null")) {
                            band = convertToListJsonBand(serverResponse);
                            debug("Banda: ");
                            System.out.println(band.toString());
                        } else {
                            System.out.println("Banda não encontrada.");
                        }
                    }
                    break;
                default:
                    out.print("Essa opção não foi encontrada.");
                    break;

            }

            out.close();
            in.close();
            read.close();
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void debug(String msg) {
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

    static Banda convertToListJsonBand(String response) {
        Banda band = new Banda();
        JSONObject jsonObject;
        JSONParser jsonParser = new JSONParser();
        JSONArray songs = new JSONArray();

        try {
            ArrayList<Musica> listsongs = new ArrayList<Musica>();
            jsonObject = (JSONObject) jsonParser.parse(response);
            songs = (JSONArray) jsonObject.get("musicas");
            for (int j = 0; j < songs.size(); j++) {
                JSONObject song = (JSONObject) songs.get(j);
                listsongs.add(new Musica((String) song.get("nome"), (String) song.get("album")));
            }
            band = new Banda(jsonObject.get("nome").toString(), listsongs);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return band;
    }
}