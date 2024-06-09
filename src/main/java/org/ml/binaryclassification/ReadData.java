package org.ml.binaryclassification;

import java.io.*;
import java.util.ArrayList;

public class ReadData {
    FileReader file;
    BufferedReader bufferedReader;
    public void openFile(String filePath){
        try {
            file =new FileReader(filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        bufferedReader=new BufferedReader(file);
    }
    public ArrayList<ArrayList<Integer>> readCSV(){
        ArrayList<ArrayList<Integer>> output= new ArrayList<>();
        String line="";
        boolean flagTitle=true;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                if (!flagTitle) {
                    String[] row = line.split(",");
                    ArrayList<Integer> temp=new ArrayList<>();
                    for (int i = 0; i < row.length-1; i++) {
                        temp.add(Integer.parseInt(row[i]));
                    }
                    if(row[row.length-1].equalsIgnoreCase("pass")){
                        temp.add(1);
                    }else{
                        temp.add(0);
                    }
                    output.add(temp);
                } else {
                    flagTitle = false;
                }
            }
        }catch (IOException e){}

        return output;
    }
}
