package demo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class utils {
    static void transform_txt2ttl(String readName, String writeName) throws IOException {
        File out_file = new File(writeName);  // 输出文件
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(out_file), "UTF-8");

        // .ttl文件的声明部分，声明每一种元素以及对应的url
        writer.write("@prefix entity:   <http://jena_demo/entity#> .\n");
        writer.write("@prefix relation:  <http://jena_demo/relation#> .\n");
        writer.write("\n");


        File f = new File(readName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-8"));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] split_data = line.split("\t");
            String temp = String.format("entity:%s\trelation:%s\tentity:%s .\n", split_data[0], split_data[2], split_data[1]);
            writer.write(temp);
            writer.flush();
        }

        reader.close();
        writer.close();
    }

    static List<String> read_test(String readName) throws IOException {
        File f = new File(readName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-8"));

        String line;
        List<String> entity_list = new ArrayList<String>();
        while ((line = reader.readLine()) != null) {
            entity_list.add(line);
        }
        reader.close();
        return entity_list;
    }
}
