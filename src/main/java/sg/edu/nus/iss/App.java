package sg.edu.nus.iss;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
        String serverHost = args[0];
        String serverPort = args[1];

        // establish connection to server - slide 8
        // *** server must be started first
        Socket socket = new Socket(serverHost, Integer.parseInt(serverPort));
        
        Scanner scan = new Scanner(System.in);
        String input = scan.next();
        String buffer = scan.nextLine();

        String msgReceived = "";

        // similar to server - slide 9
        try (InputStream is = socket.getInputStream()) {
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);

            try (OutputStream os = socket.getOutputStream()) {
                BufferedOutputStream bos = new BufferedOutputStream(os);
                DataOutputStream dos = new DataOutputStream(bos);

                // while loop here
                while (!input.equalsIgnoreCase("close")) {

                    // send message across the communication tunnel
                    dos.writeUTF(input);
                    dos.flush();

                    // receive message from server (response) and process it
                    msgReceived = dis.readUTF();
                    System.out.println(msgReceived);

                    input = scan.next();
                    buffer = scan.nextLine();
                }

                if (input.equalsIgnoreCase("close")) {
                    dos.writeUTF(input);
                    dos.flush();
                }

                // closes the output stream in reverse order
                dos.close();
                bos.close();
                os.close();

            } catch (EOFException ex) {
                ex.printStackTrace();
            }

        } catch (EOFException ex) {
            ex.printStackTrace();
            socket.close();
        }
        scan.close();
        socket.close();
    }
}
