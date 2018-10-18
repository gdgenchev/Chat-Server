package bg.chat.server;

import bg.chat.utils.Constants;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

class FileService {
    static void writeMessageToFile(String fileName, String msg) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(
                        Constants.SAVE_DIRECTORY
                                + fileName, true),
                StandardCharsets.UTF_8))) {
            writer.write(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void createFile(String fileName) {
        writeMessageToFile(fileName, "");
    }

    static String getChatRoomHistory(String fileName) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(Constants.SAVE_DIRECTORY + fileName))))
        {
            StringBuilder chatHistory = new StringBuilder();
            String line;
            boolean has = false;
            while ((line = reader.readLine()) != null) {
                chatHistory.append(line).append("\n");
                has = true;
            }
            if (has) {
                chatHistory.deleteCharAt(chatHistory.length() - 1); // remove last new line
            }
            return chatHistory.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    static void deleteFile(String roomName) {
        try {
            Files.delete(Paths.get(Constants.SAVE_DIRECTORY + roomName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
