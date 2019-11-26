package contentssecurity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Main {
	
	public static void main(String[] args) throws Exception {

        double grade[][]        = new double[4][6]; //成績行列
        double weight[][]       = new double[6][4]; //重み行列
        double pass_or_fail[][] = new double[4][4]; //合否行列
        
        FileInputStream   fis = new FileInputStream("/workspace/Contents-Security/programs/kadai1/contentssecurity/seiseki.txt");
        InputStreamReader isr = new InputStreamReader((fis), "UTF-8");
        BufferedReader    br  = new BufferedReader(isr);
        
        br.readLine();

        //成績行列に値を読み込み
        for(int i = 0; i < 4; i++){
            String input = br.readLine();
            String temp[] = input.split(",", 0);
            for(int j = 0; j < 6; j++){
                grade[i][j] = Double.parseDouble(temp[j + 1]);
            }
        }

        fis = FileInputStream()
    
        

        System.out.println("        国 数 英 理 社 内");
        
        for(int i = 0; i < 6; i++){
            System.out.printf("生徒%d  %3.0f%3.0f%3.0f%3.0f%3.0f%3.0f\n",
                i + 1, grade[i][0], grade[i][1], grade[i][2], grade[i][3], grade[i][4], grade[i][5]);
        }
        
        
	}
}

