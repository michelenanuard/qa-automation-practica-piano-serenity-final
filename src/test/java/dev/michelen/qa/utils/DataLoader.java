package dev.michelen.qa.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;

public class DataLoader {
    public static JsonNode load(String path){
        try(InputStream is = DataLoader.class.getClassLoader().getResourceAsStream(path)){
            if(is == null) throw new RuntimeException("No encontrado: " + path);
            return new ObjectMapper().readTree(is);
        } catch(Exception e){
            throw new RuntimeException("Error leyendo " + path, e);
        }
    }
}
