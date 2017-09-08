package src;

/**
 * Created by SungHere on 2017-09-08.
 */
public class gugudan {

    public static void main(String[] args) {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

                System.out.printf("%d x %d = %d \t", i + 1, j + 1, (j + 1) * (i + 1));

            }
            System.out.println();
        }




    }
}
