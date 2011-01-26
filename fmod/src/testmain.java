/**
 * Created by IntelliJ IDEA.
 * User: ben
 * Date: 1/26/11
 * Time: 8:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class testmain {

    public static void main(String[] args) {
        int[][] test = new int[4][];
        test[0] = new int[]{1,2,3,4};
        test[1] = new int[]{1,2,3};
        test[2] = new int[]{1};
        test[3] = new int[]{1,2};


        for (int i = 0; i < test.length; i++) {
            for (int j = 0; j < test[i].length; j++) {
                System.out.println(test[i][j]);
            }
        }
    }

}
