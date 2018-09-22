package bg.chat.utils;
import bg.chat.client.exceptions.WrongPasswordException;
import bg.chat.client.exceptions.WrongUsernameException;

import java.util.Arrays;
import java.io.*;

public class FileUtils {

    public static void register(String username, char[] password) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(Constants.FILE_NAME, true));
            out.write(username + " " + password + "\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkUser(String username, char[] password)
            throws WrongUsernameException, WrongPasswordException {
        try {
            BufferedReader in = new BufferedReader(new FileReader(Constants.FILE_NAME));
            String line;
            while ((line = in.readLine()) != null) {
                final String[] credentials = line.split(" ");
                if(credentials[0].equals(username)) {
                    if (!Arrays.equals(credentials[1].toCharArray(),password)) {
                        throw new WrongPasswordException("Wrong password!");
                    } else {
                        return;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new WrongUsernameException("Wrong username!");
    }
}
