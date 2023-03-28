package parceldeliverytracker;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author weiju
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileHandler {

    public static Stream<String> readLines(Path path) throws IOException {
        return Files.lines(path)
                .map(line -> line.replaceAll("'", ""))
                .map(Arrays::asList)
                .flatMap(List::stream);
    }

    public static void writeLines(Path path, Stream<String> lines) throws IOException {
        Files.write(path, lines.collect(Collectors.toList()));
    }

    public static void appendLines(Path path, Stream<String> lines) throws IOException {
        Files.write(path, lines.collect(Collectors.toList()), java.nio.file.StandardOpenOption.APPEND);
    }

    public static void writeFile(Path filePath, String newInput) throws IOException {
        Stream<String> oldStream = FileHandler.readLines(filePath);
        Stream<String> newStream = Stream.of(newInput);
        
        if (oldStream.findAny().isEmpty()) {
            FileHandler.writeLines(filePath, newStream);
        } else {
            FileHandler.appendLines(filePath,newStream);
        }
    }

}
