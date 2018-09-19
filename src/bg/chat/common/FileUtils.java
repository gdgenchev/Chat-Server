package bg.chat.common;
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

    public static boolean isRegistered(String username, char[] password) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(Constants.FILE_NAME));
            String line;
            while ((line = in.readLine()) != null) {
                final String[] credentials = line.split(" ");
                if(credentials[0].equals(username)) {
                    return Arrays.equals(credentials[1].toCharArray(),password);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
