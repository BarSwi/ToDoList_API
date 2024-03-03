package API.ToDoList_API.Utilities;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    public static @NotNull Properties loadPropertiesFromFile(String fileName){
        if(fileName.isEmpty()) return new Properties();

        try(InputStream input = loadStreamFromFile(fileName)) {
            return loadFromInputStream(input);
        }
        catch(IOException e) {
            e.printStackTrace();
            return new Properties();
        }

    }
    //Function created specifically, it would be easier to unit test the whole properties mechanism
    public static @NotNull Properties loadFromInputStream(InputStream input){
        if(input == null) return new Properties();

        Properties properties = new Properties();

        //Creating new BufferedInput, so it would be independent of the input provided.
        try (InputStream inputStream = new BufferedInputStream(input)) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
    public static InputStream loadStreamFromFile(String fileName){

        //Using classLoader.getResourceAsStream in order to get InputStream result, which should be more flexible after building app to .jar
        return PropertiesLoader.class.getClassLoader().getResourceAsStream(fileName);
    }
}
