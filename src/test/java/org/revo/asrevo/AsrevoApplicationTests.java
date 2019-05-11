package org.revo.asrevo;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class AsrevoApplicationTests {

    @Test
    public void contextLoads() {
//        0x62313632333037353734323234373734
        String hex = convertStringToHex("b162307574224774");
        System.out.println("Hex : " + hex);
//        System.out.println("ASCII : " + convertHexToString(hex));
    }

    public static String convertStringToHex(String str) {
        StringBuilder hex = new StringBuilder();
        for (char c : str.toCharArray()) {
            hex.append(Integer.toHexString((int) c));
        }
        return hex.toString();
    }

    public String convertHexToString(String hex) {

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for (int i = 0; i < hex.length() - 1; i += 2) {

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char) decimal);

            temp.append(decimal);
        }
        return sb.toString();
    }

}

