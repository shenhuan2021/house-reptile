package com.lifesmile.reptile.utils;

import com.lifesmile.reptile.cache.Cache;

import java.io.*;


public class IOUtil {

    public static void outFile(String s, String filePath) throws Exception {
        FileWriter fw = null;
        try {
            //如果文件存在，则追加内容；如果文件不存在，则创建文件
            File f = new File(filePath);
            fw = new FileWriter(f, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fw);
        pw.println(s);
        pw.flush();
        try {
            fw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void toFile(String s, String filePath) {
        File file = new File(filePath);
        try (FileOutputStream fop = new FileOutputStream(file)) {
            // if file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            // get the content in bytes
            byte[] contentInBytes = s.getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();
            System.out.println("outFile: " + filePath + " success!");
        } catch (IOException e) {
            System.out.println("outFile: " + filePath + " fail!");
            e.printStackTrace();
        }
    }

    public static void readToCache() {
        //BufferedReader是可以按行读取文件
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream("record.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String str = null;
        while (true) {
            try {
                if (!((str = bufferedReader.readLine()) != null)) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Cache.recordCache.put(str, "");
        }
        //close
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
