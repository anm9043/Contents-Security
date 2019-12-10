package contentssecurity;

import java.io.*;
import java.util.*;

public class Main {


    //CSVファイルから配列に値を読み込むためのメソッド
    static void inputArray(double[][] a, FileInputStream fis){
        try{
            final int row = a.length;     //行数
            final int clm = a[0].length;  //列数

            InputStreamReader isr = new InputStreamReader((fis), "UTF-8");
            BufferedReader    br  = new BufferedReader(isr);

            br.readLine(); //1行目を捨てる

            for(int i = 0; i < row; i++){
                String input = br.readLine();
                String temp[] = input.split(",", 0);
                for(int j = 0; j < clm; j++){
                    a[i][j] = Double.parseDouble(temp[j + 1]);
                }
            }
        }catch(IOException e){
            System.err.println(e.getMessage());
        }

    }

    static void generateRandomArray(double[][] a){
        final int row = a.length;
        final int clm = a[0].length;

        Random rand = new Random();

        for(int i = 0; i < row; i++){
            for(int j = 0; j < clm; j++){
                a[i][j] = (double)rand.nextInt(100);
            }
        }
    }

    //行列aと行列bの積を行列cに書き込むメソッド
    static boolean matrixProduct(double[][] a, double[][] b, double[][] c){
        final int a_row = a.length;      //aの行数
        final int a_clm = a[0].length;   //aの列数
        final int b_row = b.length;      //bの行数
        final int b_clm = b[0].length;   //bの列数
        final int c_row = c.length;      //cの行数
        final int c_clm = c[0].length;   //cの列数

        if((a_clm != b_row) || (a_row != c_row) || (b_clm != c_clm))
            return false;  //行列の積が計算出来ない
        else{
            for(int i = 0; i < a_row; i++){
                for(int j = 0; j < b_clm; j++){
                    c[i][j] = 0.0;
                    for(int k = 0; k < a_clm; k++){
                        c[i][j] += a[i][k] * b[k][j];
                    }
                }
            }
            return true;
        }
    }

    //合否行列を生成するメソッド
    static boolean generatePOFmatrix(double[][] apttd, double[] bottom, double[][] pof){
        final int apttd_row = apttd.length;
        final int apttd_clm = apttd[0].length;
        final int pof_row   = pof.length;
        final int pof_clm   = pof[0].length;

        if((apttd_clm != bottom.length) || (apttd_row != pof_row) || (apttd_clm != pof_clm)){
            return false;  //合否行列を生成出来ない
        }else{
            for(int i = 0; i < apttd_row; i++){
                for(int j = 0; j < apttd_clm; j++){
                    pof[i][j] = (apttd[i][j] >= bottom[j]) ? 1.0 : 0.0;
                }
            }
            return true;
        }
    }


	public static void main(String[] args) throws Exception {

        final int n_stdnt = 4;  //生徒数
        final int n_sbjct = 6;  //科目数
        final int n_schol = 4;  //高校数

        final double grade[][]  = new double[n_stdnt][n_sbjct]; //成績行列
        final double weight[][] = new double[n_sbjct][n_schol]; //重み行列
        final double btmln[][]  = new double[1][n_schol];       //合格最低点行列
        final double apttd[][]  = new double[n_stdnt][n_schol]; //適性行列 Aptitude
        final double pof[][]    = new double[n_stdnt][n_schol];//合否行列 Pass or Fail


        // 成績行列に値を読み込み
        FileInputStream   fis = new FileInputStream("./contentssecurity/seiseki.txt");
        inputArray(grade, fis);

        // 重み行列に値を読み込み
        fis = new FileInputStream("./contentssecurity/omomi.txt");
        inputArray(weight, fis);

        //合格最低点行列に値を読み込み
        fis = new FileInputStream("./contentssecurity/saiteiten.txt");
        inputArray(btmln, fis);

        if(matrixProduct(grade, weight, apttd)){
            System.out.println("適性行列");
            System.out.println("      高校A 高校B 高校C 高校D");
            for(int i = 0; i < n_stdnt; i++){
                System.out.print("生徒" + (i + 1) + " ");
                for(int j = 0; j < n_schol; j++){
                    System.out.printf("%5.1f ", apttd[i][j]);
                }
                System.out.println();
            }

            if(generatePOFmatrix(apttd, btmln[0], pof)){
                System.out.println();
                System.out.println("合否行列");
                System.out.println("      高校A 高校B 高校C 高校D");
                for(int i = 0; i < n_stdnt; i++){
                    System.out.print("生徒" + (i + 1) + " ");
                    for(int j = 0; j < n_schol; j++){
                        System.out.print(pof[i][j] == 0.0 ? "　否　" : "　合　");
                    }
                    System.out.println();
                }

                //ファイル出力
                //https://www.sejuku.net/blog/20960
                try{
                    //合否行列
                    FileWriter file = new FileWriter ("./contentssecurity/gouhi.txt");
                    PrintWriter pw  = new PrintWriter(new BufferedWriter(file));

                    //1行目
                    pw.print("合否行列");
                    char ch = 'A';
                    for(int i = 0; i < n_schol; i++, ch++){
                        pw.printf(",高校%c", ch);
                    }
                    pw.println();

                    //2行目以降
                    for(int i = 0; i < n_stdnt; i++){
                        pw.printf("生徒%d", i + 1);
                        for(int j = 0; j < n_schol; j++){
                            pw.print("," + ((pof[i][j] == 0.0) ? "否" : "合"));
                        }
                        pw.println();
                    }

                    pw.close();



                    //適性行列
                    file = new FileWriter("./contentssecurity/tekisei.txt");
                    pw   = new PrintWriter(new BufferedWriter(file));

                    //1行目
                    pw.print("適性行列");
                    ch = 'A';
                    for(int i = 0; i < n_schol; i++, ch++){
                        pw.printf(",高校%c", ch);
                    }
                    pw.println();

                    //2行目以降
                    for(int i = 0; i < n_stdnt; i++){
                        pw.printf("生徒%d", i + 1);
                        for(int j = 0; j < n_schol; j++){
                            pw.printf(",%.1f", apttd[i][j]);
                        }
                        pw.println();
                    }

                    pw.close();


                } catch(IOException e){
                    e.printStackTrace();
                }


            }

        }

	}
}
