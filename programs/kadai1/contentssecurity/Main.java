package contentssecurity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Main {
	
	public static void main(String[] args) throws Exception {

        double grade[][]        = new double[4][6]; //成績行列
        double weight[][]       = new double[6][4]; //重み行列
        double pass_or_fail[][] = new double[4][4]; //合否行列
        
        FileInputStream   fis = new FileInputStream("seiseki.txt");
        InputStreamReader isr = new InputStreamReader((fis), "UTF-8");
        BufferedReader    br  = new BufferedReader(isr);
        
        for(int i = 0; i < 4; i++){
            String input = br.readLine();
            String temp[] = input.split(",", 0);
            for(int j = 0; j < 4; j++){
                grade[i][j] = Double.parseDouble(temp[j]);
            }
        }
        
        System.out.println("国,数,英,理,社,内");
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 6; j++){
                System.out.printf("生徒%d  %3d%3d%3d%3d%3d%3d",
                    i + 1, grade[i][0], grade[i][1], grade[i][2], grade[i][3], grade[i][4], grade[i][5]);
            }
        }
        
	}
}

