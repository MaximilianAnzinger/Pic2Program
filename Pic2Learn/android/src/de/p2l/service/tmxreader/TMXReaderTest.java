package de.p2l.service.tmxreader;

public class TMXReaderTest {

    public static void main(String[] args){
        TMXReader tmx = new TMXReader("android/assets/maps/vszs.tmx");
        System.out.println(boolArrayToStringKoord(tmx.computeFieldStatus()));
        System.out.println(boolArrayToString(tmx.computeFieldStatus()));
    }

    public static String boolArrayToStringKoord(boolean[][] arr){
        String out = "";
        for(int i = 0; i<arr[0].length; i++){
            for(int j = 0; j<arr.length; j++){
                out += "x: "+j+", y: "+i+": "+arr[j][i] + "\t";
            }
            out += "\n";
        }
        return out;
    }

    public static String boolArrayToString(boolean[][] arr){
        String out = "";
        for(int i = 0; i<arr[0].length; i++){
            for(int j = 0; j<arr.length; j++){
                out += arr[j][i] + "\t";
            }
            out += "\n";
        }
        return out;
    }
}
