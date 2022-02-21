import Requests.ReqHandler;
import Requests.Request;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.awt.BorderLayout.LINE_END;


public class Main {

    public static void main(String args[]) throws IOException, SQLException {
        int port = 10001;
        ServerSocket serverSocket = new ServerSocket(port);
        Request request = new Request();
        System.out.println("Server listens on : " + port + "\n");
        int counter = 0;


        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connection created to Client\n");

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            String input;
            while(in.ready())
            {
                input = in.readLine();
                String[] splittedInput = input.split(" ");

                if (counter == 0)
                {
                    request.setMethod(splittedInput[0]);
                    request.setRoute(splittedInput[1]);
                }
                else
                {
                    try
                    {
                        request.setContentLength(Integer.parseInt(splittedInput[1]));
                    }
                    catch (NumberFormatException e)
                    {
                        continue;
                    }

                    int cLength = request.getContentLength();
                    if (cLength > 0)
                    {
                        char[] cBuff = new char[cLength + 2];
                        in.read(cBuff, 0, cLength + 2);
                        request.setContent(new String(cBuff));
                    }
                }

                counter++;

            }

            ReqHandler reqHandler = new ReqHandler(request);
            System.out.println("Connection to client is terminated\n");
            out.close();
            in.close();
            clientSocket.close();
        }
    }
}
