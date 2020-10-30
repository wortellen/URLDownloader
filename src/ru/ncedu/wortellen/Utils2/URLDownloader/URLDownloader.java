package ru.ncedu.wortellen.Utils2.URLDownloader;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class URLDownloader {
    public static String filePath;
    public static File out;
    public static URL url;
    public static String typeOfFile;
    public static String charset;


    public static void main(String[] args) throws Exception {
        setURL(args[0]);
        if (args.length >= 2) {
            setFilePath(args[1]);
        } else setFilePath("1");
        setFile();
        download();
        Scanner in = new Scanner(System.in);
        System.out.println("If you want to open the downloaded file, enter 1");
        if (in.next().equals("1"))
            openFile();
    }

    public static void setURL(String link) throws MalformedURLException {
        url = new URL(link);
    }

    public static void setFilePath(String path) {
        if ((url.getPath().equals("")) || url.getPath().equals("/")) {
            if (path.equals("1")) {
                filePath = "index.html";
            } else filePath = path + "\\index.html";
        } else {
            String tmp = url.getPath().substring(url.getPath().lastIndexOf("/") + 1);
            if (path.equals("1")) {
                filePath = tmp;
            } else filePath = path + "\\" + tmp;
        }
    }

    public static void setFile() {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            System.out.println("File with the same name already exists, replace it (1) or set a new name(2)?");
            Scanner in = new Scanner(System.in);
            int tmp = in.nextInt();
            if (tmp == 1) {
                out = new File(filePath);
            } else if (tmp == 2) {
                System.out.println("Enter new file name");
                String newName = in.next();
                if (filePath.lastIndexOf(".") != -1)
                    filePath = filePath.substring(0, filePath.lastIndexOf("\\") + 1) + newName + filePath.substring(filePath.lastIndexOf("."));
                else filePath = filePath.substring(0, filePath.lastIndexOf("\\") + 1) + newName;
                out = new File(filePath);
            }
        } else out = new File(filePath);
    }

    public static void openFile() throws Exception {
        Desktop desktop = Desktop.getDesktop();
        System.err.println(desktop.isSupported(Desktop.Action.OPEN));
        desktop.open(new java.io.File(filePath));
    }

    public static void download() {
        try {
            System.out.println("Download start");
            URLConnection urlc = url.openConnection();
            typeOfFile = urlc.getContentType();
            if (typeOfFile.contains("charset"))
                charset = typeOfFile.substring(typeOfFile.indexOf("charset") + 8);
            BufferedInputStream in = new BufferedInputStream(urlc.getInputStream());
            FileOutputStream fos = new FileOutputStream(out);
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = in.read(buffer, 0, 1024)) >= 0) {
                bout.write(buffer, 0, read);
            }
            bout.close();
            in.close();
            System.out.println("Download complete");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}