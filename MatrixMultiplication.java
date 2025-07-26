
public class MatrixMultiplication extends Thread {
    private int row;
    private static final int SIZE = 3;

    static int[][] A = {
        {1, 2, 3},
        {4, 5, 6},
        {7, 8, 9}
    };

    static int[][] B = {
        {7, 8, 9},
        {4, 5, 6},
        {1, 2, 3}
    };

    static int[][] result = new int[SIZE][SIZE];

    MatrixMultiplication(int row) {
        this.row = row;
    }

    public void run() {
        for (int col = 0; col < SIZE; col++) {
            result[row][col] = 0;
            for (int k = 0; k < SIZE; k++) {
                result[row][col] += A[row][k] * B[k][col];
            }
        }
    }

    public static void main(String[] args) {
        MatrixMultiplication[] threads = new MatrixMultiplication[SIZE];

        for (int i = 0; i < SIZE; i++) {
            threads[i] = new MatrixMultiplication(i);
            threads[i].start();
        }

        for (int i = 0; i < SIZE; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Resultant Matrix:");
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(result[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
