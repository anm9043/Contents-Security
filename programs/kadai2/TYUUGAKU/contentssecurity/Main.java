package contentssecurity;

import java.io.*;
import java.net.*;
import java.util.Random;

public class Main {

	/**自分が中学ならば"TYUUGAKU"，予備校ならば"YOBIKOU"と入力*/
	static String NAME = "TYUUGAKU";

	/**ペアの相手のIPを入力*/
	static String PARTNERSIP = "192.168.128.223";

	/**通信を行い際に使用します*/
	static Connector connector = new Connector(NAME,PARTNERSIP);


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


	//乱数行列を生成
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

	//行列aを左右に分けて行列bと行列cに書き込む
	static boolean divideMatrixRL(double[][] a, double[][] b, double[][] c){
		final int a_row = a.length;      //aの行数
		final int a_clm = a[0].length;   //aの列数
		final int b_row = b.length;      //bの行数
		final int b_clm = b[0].length;   //bの列数
		final int c_row = c.length;      //cの行数
		final int c_clm = c[0].length;   //cの列数

		if(a_clm % 2 != 0 || a_clm / 2 != b_clm || b_clm != c_clm || !(a_row == b_row && b_row == c_row)){
			return false;
		}else{
			for(int i = 0; i < a_row; i++){
				for(int j = 0; j < a_clm / 2; j++){
					b[i][j] = a[i][j];
				}
				for(int j = a_clm / 2; j < a_clm; j++){
					c[i][j - a_clm / 2] = a[i][j];
				}
			}
			return true;
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

	public static void main(String[] args) throws Exception {

		final int n_stdnt = 4;  //生徒数
		final int n_sbjct = 6;  //科目数
		final int n_schol = 4;  //高校数

		final double[][] grade = new double[n_stdnt][n_sbjct];

		// 成績行列に値を読み込み
		FileInputStream   fis = new FileInputStream("./contentssecurity/seiseki.txt");
		inputArray(grade, fis);

		final double[][] rdmMx = new double[n_sbjct][n_sbjct]; //乱数行列
		generateRandomArray(rdmMx);

		connector.sendTable(rdmMx);

		final double[][] rdmMxL = new double[n_sbjct][n_sbjct / 2]; //乱数行列の左半分
		final double[][] rdmMxR = new double[n_sbjct][n_sbjct / 2]; //乱数行列の右半分

		if(divideMatrixRL(rdmMx, rdmMxL, rdmMxR)){ //エラー1に対応
			final double[][] a_1 = new double[n_stdnt][n_sbjct / 2];
			final double[][] a_2 = new double[n_stdnt][n_schol];

			if(matrixProduct(grade, rdmMxL, a_1)){ //エラー2に対応
				connector.sendTable(a_1);
				final double[][] temp = new double[n_stdnt][n_sbjct / 2];
				if(matrixProduct(grade, rdmMxR, temp)){ //エラー3
					double[][] b = null;
					b = connector.getTable();
					// テスト用
					// System.out.println("行列B'");
					// System.out.println(b.length + "行" + b[0].length + "列");
					if(matrixProduct(temp, b, a_2)){ //エラー4
						connector.sendTable(a_2);

						double[][] pof = null; //合否行列
						pof = connector.getTable();

						//合否行列をファイルに書き込み
						//https://www.sejuku.net/blog/20960
						try{
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
						} catch(IOException e){
							e.printStackTrace();
						}



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
					}else{
						System.out.println("エラー4");
					}
				}else{
					System.out.println("エラー3");
				}
			}else{
				System.out.println("エラー2");
			}
		}else{
			System.out.println("エラー1");
		}

		/**通信を行う際に使用します*/
		connector.endConnection();
	}
}
