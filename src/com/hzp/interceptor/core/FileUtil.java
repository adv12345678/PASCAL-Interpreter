package com.hzp.interceptor.core;

import java.io.*;

public class FileUtil {
    public static String getFileContent(String fileName) throws IOException {
        File file = new File(fileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line = null;
        StringBuilder result = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null){
            result.append(line);
        }
        return result.toString();
    }
}
