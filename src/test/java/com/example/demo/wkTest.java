package com.example.demo;

import java.io.IOException;

public class wkTest {
    public static void main(String[] args) {
        String cmd = "f:/wkhtml/wkhtmltopdf/bin/wkhtmltoimage https://www.nowcoder.com f:/wkhtml/wkimgs/3.png";
        try {
            Runtime.getRuntime().exec(cmd);
            System.out.println("ok");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
