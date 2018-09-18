package bg.chat.common;

import java.io.*;

import static bg.chat.common.Constants.FILE_NAME;

public class User {
    private final String username;
    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void register() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(FILE_NAME, true));
            out.write(username + " " + password + "\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isRegistered() {
        try {
            BufferedReader in = new BufferedReader(new FileReader(FILE_NAME));
            String line;
            while ((line = in.readLine()) != null) {
                final String[] credentials = line.split(" ");
                if(credentials[0].equals(username)) {
                    return credentials[1].equals(password);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
