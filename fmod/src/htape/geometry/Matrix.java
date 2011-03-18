package htape.geometry;

import java.security.PublicKey;

/**
 * Created by ben, on 3/11/11 at 10:56 AM
 */
public class Matrix {

    double m[][];

    public Matrix(int rows, int cols) {
        m = new double[rows][cols];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                m[i][j] = 0;
            }
        }
    }

    public Matrix(double[][] doubles) {
        m = doubles;
    }

    public Point toPoint(){
        return new Point(m[0][0],m[1][0],m[2][0]);
    }

    public Matrix mult(Point p){
        Matrix x = new Matrix(4,1);
        x.getArray()[0][0] = p.getX();
        x.getArray()[1][0] = p.getY();
        x.getArray()[2][0] = p.getZ();
        x.getArray()[3][0] = 1;
        return mult(x);
    }

    public int rowCount(){
        return m.length;
    }

    public int colCount(){
        return m[0].length;
    }

    public Matrix mult (Matrix B) {
      if (B.rowCount() != colCount()) {
         throw new IllegalArgumentException("Matrix inner dimensions must agree.");
      }
      Matrix X = new Matrix(rowCount(),B.colCount());
      double[][] C = X.getArray();
      double[] Bcolj = new double[colCount()];
      for (int j = 0; j < B.colCount(); j++) {
         for (int k = 0; k < colCount(); k++) {
            Bcolj[k] = B.getArray()[k][j];
         }
         for (int i = 0; i < rowCount(); i++) {
            double[] Arowi = m[i];
            double s = 0;
            for (int k = 0; k < colCount(); k++) {
               s += Arowi[k]*Bcolj[k];
            }
            C[i][j] = s;
         }
      }
      return X;
   }

    public double[][] getArray() {
        return m;
    }

    public static Matrix rotX(double ang){
        return new Matrix(new double[][]{
            {1,0,0,0},
            {0,Math.cos(ang),-Math.sin(ang),0},
            {0,Math.sin(ang),Math.cos(ang),0},
            {0,0,0,1}
        });
    }
    public static Matrix rotZ(double ang){
        return new Matrix(new double[][]{
            {Math.cos(ang),-Math.sin(ang),0,0},
            {Math.sin(ang),Math.cos(ang),0,0},
            {0,0,1,0},
            {0,0,0,1}
        });
    }
    public static Matrix rotY(double ang){
        return new Matrix(new double[][]{
            {Math.cos(ang) ,0,Math.sin(ang),0},
            {0,1,0,0},
            {-Math.sin(ang),0,Math.cos(ang),0},
            {0,0,0,1}
        });
    }



   public void print () {
       System.out.println();
      for (int i = 0; i < rowCount(); i++) {
         for (int j = 0; j < colCount(); j++) {
            String s = String.valueOf(m[i][j]); // format the number
            int padding = Math.max(1,10-s.length()); // At _least_ 1 space
            for (int k = 0; k < padding; k++)
                System.out.print(' ');
            System.out.print(s);
         }
         System.out.println();
      }
      System.out.println();   // end with blank line
   }


    
}
