import org.apache.commons.math3.analysis.function.Exp;
import org.apache.commons.math3.analysis.function.Pow;
import org.apache.commons.math3.linear.ArrayRealVector;

/**
 * Created by mlevitin on 26.11.2016.
 */
public class euclidean {
    protected static double a1[] = {
            0.09220985, -0.0681314f , -0.11580598,  0.07313379, -0.07345156,
            0.02329779,  0.05814262, -0.05309539,  0.20792033, -0.12362318,
            0.26324084,  0.03866995,  0.07608874, -0.10223258,  0.0657178,
            -0.08177867, -0.08531126, -0.09971812, -0.04912016,  0.02949078,
            -0.18137185,  0.17208834,  0.06115345,  0.04461022, -0.14159331,
            -0.0271113,  0.02166164,  0.03833164, -0.06509953, -0.07349667,
            0.02184311,  0.11115962,  0.08505372,  0.00255523, -0.06974252,
            0.22377653,  0.01883085,  0.1751471,  0.08599104,  0.21865158,
            0.07352214, -0.07516863, -0.14694889,  0.11252031, -0.03235815,
            0.05181734, -0.08017924, -0.03208954, -0.03364349, -0.05319224,
            -0.00792952,  0.02240941, -0.13211289, -0.09546722, -0.09668011,
            0.14003041, -0.05520651,  0.09499975,  0.00394137, -0.14820081,
            -0.03258764,  0.05555704, -0.04968469, -0.0220145, -0.02891627,
            -0.00094346, -0.07328547,  0.01962685, -0.073447,  0.03910528,
            0.05717066,  0.13144678,  0.08889023, -0.11668456, -0.04151125,
            -0.03455345,  0.10325407,  0.01001143,  0.07667451,  0.08012081,
            0.06651299, -0.11430524,  0.03788028, -0.12235609, -0.15135401,
            -0.0307548,  0.05385722, -0.06491732,  0.1276798,  0.06597276,
            -0.02364791, -0.07002126,  0.02779831, -0.04230236,  0.03175509,
            0.11071441, -0.0420349,  0.01997653, -0.03115777,  0.0024299,
            0.10206569,  0.08081728,  0.04906504, -0.05319322,  0.06197476,
            -0.10122427, -0.10369578, -0.07127877, -0.05189025,  0.0649693,
            -0.01395369, -0.03806365,  0.01544157,  0.03515985,  0.18239044,
            0.09935737, -0.04691097,  0.10817883,  0.14009495, -0.00141741,
            0.04819384, -0.10831979,  0.02511743, -0.02743166,  0.05107384,
            -0.07011004,  0.05036785, -0.05941105
    };

    protected static double a2[] = {
            -1.03441633e-01,   1.53014690e-01,  -1.54231146e-01,
            -6.77782148e-02,   2.40469072e-02,  -8.14208835e-02,
            9.92173515e-03,  -6.20239563e-02,   1.44678657e-03,
            -2.89069135e-02,  -4.80061881e-02,  -1.87309459e-01,
            1.17532566e-01,  -4.41507846e-02,   8.23602602e-02,
            -5.91803864e-02,   1.10152438e-01,   1.20860422e-02,
            -1.09644599e-01,  -8.11566189e-02,   2.87953522e-02,
            -4.15984951e-02,  -7.18034729e-02,   2.05751304e-02,
            -5.12872934e-02,  -3.29578444e-02,   5.38355336e-02,
            3.43771502e-02,   1.57101125e-0,  -1.84413657e-01,
            4.06999923e-02,  -3.87158692e-02,   1.00732461e-01,
            7.72371367e-02,   1.70414522e-02,  -1.64993450e-01,
            -2.09074080e-01,   1.59548353e-02,   1.86439291e-01,
            -1.27423704e-01,  -4.34272401e-02,   4.03482243e-02,
            -5.78708888e-04,  -2.29890153e-01,   2.53552552e-02,
            9.66331512e-02,  -2.45425656e-01,   4.67818184e-03,
            -9.84559208e-02,   2.31227875e-02,   1.65591948e-02,
            -2.90514324e-02,  -2.30458751e-02,  -6.81834072e-02,
            8.10528696e-02,  -1.86183915e-01,  -6.78584501e-02,
            1.05642892e-01,  -3.37494873e-02,   9.08134133e-02,
            -4.11454812e-02,  -2.96536274e-02,   6.74480479e-03,
            -4.15030643e-02,  -1.02679975e-01,  -1.41539471e-02,
            8.68770257e-02,   5.55338264e-02,  -2.41220128e-04,
            -6.71948865e-02,  -1.44440442e-01,  -2.06909459e-02,
            8.27090442e-02,  -1.31205559e-01,   3.84458788e-02,
            1.49943128e-01,   8.56777728e-02,  -1.16701704e-02,
            -6.10435829e-02,  -1.54895103e-02,   5.22778109e-02,
            -3.97320539e-02,   1.05791599e-01,   1.37688324e-01,
            8.95388704e-03,   2.67909747e-02,  -4.29196879e-02,
            -2.71562040e-02,  -2.24354062e-02,  -1.19858354e-01,
            8.92192796e-02,  -2.52070669e-02,  -9.60771739e-03,
            7.24369660e-02,   1.27345815e-01,  -1.58298418e-01,
            -1.50228173e-01,  -1.32474259e-01,  -5.78058744e-03,
            1.48645669e-01,  -5.85128032e-02,   5.65801896e-02,
            -1.93531264e-03,   1.97039843e-02,  -1.40358821e-01,
            9.85313430e-02,  -9.93729159e-02,  -9.70834959e-03,
            -8.33395645e-02,  -1.87235959e-02,  -1.93903577e-02,
            8.09634700e-02,  -4.74501029e-02,  -6.77941591e-02,
            5.42649515e-02,   5.18081011e-03,   4.85571325e-02,
            7.19712153e-02,   5.61294816e-02,   6.17671832e-02,
            -4.25198562e-02,  -6.03984520e-02,  -2.10957751e-02,
            3.10680047e-02,   1.53816923e-01,   3.12109143e-02,
            -6.00138977e-02,  -6.41409308e-02};

    static double handmade() {
        euclidean euc = new euclidean();
        double dist = euc.distance(a1, a2);
        System.out.println("handmade distance:"+ dist);
        return dist;

    }

    private static double distance(double[] a, double[] b) {
        double diff_square_sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            diff_square_sum += (a[i] - b[i]) * (a[i] - b[i]);
        }
        return  Math.sqrt(diff_square_sum);
        //return diff_square_sum;
    }


    private static double commonsmath()
    {


        ArrayRealVector arv1 = new ArrayRealVector(a1);
        ArrayRealVector arv2 = new ArrayRealVector(a2);


        double mult = 0.7f;
        int power = 4;
        double dist = arv2.getDistance(arv1);
        System.out.println("commons distance = "+dist);

        Exp exp = new Exp();
        Pow pow = new Pow();

        double norm = 1 / exp.value(pow.value(mult*dist, power));
        System.out.println("normalized= "+ norm);
        return dist;
    }
    public static void main(String argz[])
    {
        double a1= handmade();
        double a2 = commonsmath();

        System.out.println("distance difference is "+(a2-a1));
    }

}
