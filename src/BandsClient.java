import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JOptionPane;
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
        
        userOptionToServer = read.readLine();
        debug("Sending '" + userOptionToServer + "'");
        out.print(userOptionToServer + "\r\n"); // send to server
        out.flush();

        String serverResponse = null;
        while ((serverResponse = in.readLine()) != null)
            debug(serverResponse); // read from server and print it.

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
}