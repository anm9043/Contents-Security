import java.util.Random;

public class Test{

    // 行列aと行列bの積を行列cに書き込むメソッド
    static boolean matrixProduct(double[][] a, double[][] b, double[][] c) {
        final int a_row = a.length; // aの行数
        final int a_clm = a[0].length; // aの列数
        final int b_row = b.length; // bの行数
        final int b_clm = b[0].length; // bの列数
        final int c_row = c.length; // cの行数
        final int c_clm = c[0].length; // cの列数

        if ((a_clm != b_row) || (a_row != c_row) || (b_clm != c_clm))
            return false; // 行列の積が計算出来ない
        else {
            for (int i = 0; i < a_row; i++) {
                for (int j = 0; j < b_clm; j++) {
                    c[i][j] = 0.0;
                    for (int k = 0; k < a_clm; k++) {
                        c[i][j] += a[i][k] * b[k][j];
                    }
                }
            }
            return true;
        }
    }


    public static void main(final String[] args) {
        double[][] a = 
            {{2.0, 1.0, 0.},
             {3.0, 5.0, 1.0},
             {6.0, 0.0, 1.0}};
        double[][] b = 
            {{0.0, 2.0, 1.0},
             {3.0, 0.0, 4.0},
             {1.0, 2.0, 3.0}};
        double[][] c = new double[3][3];

        System.out.println("行列a");
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                System.out.printf("%3.0f", a[i][j]);
            }
            System.out.println();
        }

        System.out.println("行列b");
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                System.out.printf("%3.0f", b[i][j]);
            }
            System.out.println();
        }

        if(matrixProduct(a, b, c)){
            System.out.println("行列c");
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    System.out.printf("%3.0f", c[i][j]);
                }
                System.out.println();
            }
        }

        
    }
}