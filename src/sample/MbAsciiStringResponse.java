package sample;

import static java.lang.Integer.parseInt;

/**
 * Класс обработки ответа от устройства по ModBus ASCII
 */
public class MbAsciiStringResponse {
    private String response = "";
    private int [] dataResponse;
    private boolean [] dataCoilResponse;

    MbAsciiStringResponse (){};

    MbAsciiStringResponse (String string) {
        if (string.codePointAt(0) != 58 && string.codePointAt(string.length() - 1 ) != 10 && string.codePointAt(string.length() - 2 ) != 13)
            throw new IllegalArgumentException((" Wrong response format - ") + string);

        string = string.replace("\n","");
        string = string.replace("\r","");

        this.response = string;
        dataResponse();

    }

    MbAsciiStringResponse (byte [] bytes) {

        if (bytes[0] != 58 && bytes[bytes.length - 1] != 10 && bytes[bytes.length - 2] != 13)
            throw new IllegalArgumentException(" Wrong response format" + bytes[0] + "-" + bytes[bytes.length - 1]+ "-" + bytes[bytes.length - 2]);

        char ch;
        String string = "";
        for (byte i : bytes) {
            ch = (char) i;
            string = string + Character.toString(ch);
        }
        string = string.replace("\n","");
        string = string.replace("\r","");

        this.response = string;
        dataResponse();
    }

    MbAsciiStringResponse (char [] chars) {
        if (chars[0] != (char) 58 && chars[chars.length - 1] != (char) 10 && chars[chars.length - 2] != (char) 13)
            throw new IllegalArgumentException(" Wrong response format");

        String string = "";
        for (char ch : chars) {
            string = string + Character.toString(ch);
        }

        string = string.replace("\n","");
        string = string.replace("\r","");
        this.response = string;
        dataResponse();
    }

    public void setResponse(String response) {
        this.response = response;
    }



    public String getResponse () {
        return this.response;
    }

    /**
     *Получение массива слов.
     * @return  преобразованый массив слов (в десятичной форме)
     */
    public int [] getDataResponse (){
        return this.dataResponse;
    }

    /**
     *Получение массива бит (true = 1, foalse = 0) младший адресс вперед.
     * @return resp преобразованый массив байт
     */
    public boolean [] getDataCoilResponse () {
        return this.dataCoilResponse;
    }

    /**
     * Определение LCR проверочной суммы по строке ответа хранящияся в экземпляре класса.
     * Экземпляр класса @MbAsciiString должен быть проинициализирован.
     * В случае отсутсвия строки ответа выбрасывает исключение IllegalArgumentException("response string is not initialized")
     */

    public String getLrcCheckSum () {
        String resp = this.response;
        String str ;
        char [] respCharArray = resp.toCharArray();
        char  chHigh;
        char chLow;
        Integer getValue;
        Integer value = 0;

        if (respCharArray.length == 0) throw new IllegalArgumentException("response string is not initialized");

            for (int i = 1; i < respCharArray.length - 2; i=i+2){
                chHigh = respCharArray [i];
                chLow = respCharArray [i+1];

                try {
                    getValue = parseInt(Character.toString(chHigh)+ Character.toString(chLow),16);
                    value += getValue;
                }
                catch (NumberFormatException ex){
                    System.out.println("Invalid input value (no HEX)" + "--> " + chHigh + " --> " + chLow);
                }
            }

            value = 255 - value + 1;
            str = Integer.toHexString(value).toUpperCase();
            str = str.substring(str.length() - 2);
            //System.out.println(str);

        return str;
    }
    /**
     * Статический метод.
     * Определение LRC проверочной суммы по строке.
     * Экземпляр класса @MbAsciiString должен быть проинициализирован.
     * В случае отсутсвия строки ответа выбрасывает исключение IllegalArgumentException("response string is not initialized")
     */
//todo необходимо стандартизировать просчет суммы для различных случаев.
    public static String getLrcCheckSum (String response) {
        String resp = response;
        String str ;
        char [] respCharArray = resp.toCharArray();
        char  chHigh;
        char chLow;
        Integer getValue;
        Integer value = 0;

        if (respCharArray.length == 0) throw new IllegalArgumentException("response string is not initialized");

        for (int i = 1; i < respCharArray.length - 2; i=i+2){
            chHigh = respCharArray [i];
            chLow = respCharArray [i+1];

            try {
                getValue = parseInt(Character.toString(chHigh)+ Character.toString(chLow),16);
                value += getValue;
            }
            catch (NumberFormatException ex){
                System.out.println("Invalid input value (no HEX)");
            }
        }

        value = 255 - value + 1;
        str = Integer.toHexString(value).toUpperCase();
        str = str.substring(str.length() - 2);
        //System.out.println(str);

        return str;
    }

    /**
     * Статический метод.
     * Определение LCR проверочной суммы по массиву байт.
     * Экземпляр класса @MbAsciiString должен быть проинициализирован.
     * В случае отсутсвия строки ответа выбрасывает исключение IllegalArgumentException("response string is not initialized")
     */
    public  static String getLrcCheckSum (byte [] bytes) {

        String str;
        char  chHigh;
        char chLow;
        Integer getValue;
        Integer value = 0;

        if (bytes [0] == 58 && bytes [bytes.length - 1] == 10 && bytes[bytes.length - 2] == 13)
            throw new IllegalArgumentException
                    (" Wrong response format - " + bytes [0]+" -- "+ bytes [bytes.length - 1]+" -- " + bytes[bytes.length - 2]+ " !!!");

            for (int i = 1; i < bytes.length - 2; i=i+2){
                chHigh = (char) bytes [i];
                chLow = (char) bytes [i+1];

                try {
                    getValue = parseInt(Character.toString(chHigh)+ Character.toString(chLow),16);
                    value += getValue;
                }
                catch (NumberFormatException ex){
                    System.out.println("Invalid input value (no HEX)");
                }
            }

            value = 255 - value + 1;
            str = Integer.toHexString(value).toUpperCase();
            str = str.substring(str.length() - 2);
            System.out.println(str);


        return str;

    }

    /**
     *
     * @return int - десятичное представление шестнадцатичного значения адресса устройства в сети Mb
     */
    public int getDeviceAddress () {
        return parseInt(this.response.substring(1,3),16);
    }

    /**
     *
     * @return int - десятичное представление шестнадцатичного значения функции запроса Mb
     */
    public int getResponseFunction () {
        return parseInt(this.response.substring(3,5),16);
    }
    /**
     *
     * @return int - десятичное представление шестнадцатичного значения количества байт Mb
     */
    public int getNumberOfBytes () {
        return parseInt(this.response.substring(5,7),16);
    }

    /**
     *
     * @return int [] - массив данных с запроса длинной равной количиству считаных слов.
     */
    private void dataResponse (){
        int iter = 0;
        int [] datResp;
        String resp = this.response.substring(7,response.length() - 2);
        if (getResponseFunction() == 3 || getResponseFunction() == 4) {
            datResp = new int[getNumberOfBytes()/2];
            for (int i = 0; i < getNumberOfBytes() / 2; i++) {
                datResp[i] = parseInt(resp.substring(iter, iter + 4), 16);
                iter += 4;
            }
        }
         else if (getResponseFunction() == 1 || getResponseFunction() == 2) {
            datResp = new int[getNumberOfBytes()];
            for (int i = 0; i < getNumberOfBytes(); i++) {
                datResp[i] = parseInt(resp.substring(iter, iter + 2), 16);
                iter += 2;
            }
            this.dataCoilResponse = coilBitArryResponse(datResp);
        } else {
            datResp = new int[0];
        }

        this.dataResponse = datResp;
    }

    /**
     * Получение массива бит (true = 1, foalse = 0) младший адресс вперед.
     * @param datResp массив значений слов считаных с устройства
     * @return resp преобразованый массив байт
     */

    private boolean [] coilBitArryResponse (int [] datResp){
        String str;
        boolean [] resp = new boolean[datResp.length * 8];
        for (int i:datResp) {
            str = Integer.toBinaryString(i);
            int iter = 7;
            for (char ch:str.toCharArray()) {
                switch (ch) {
                    case '1' : resp [iter] = true;
                    break;
                    case '0' : resp [iter] = false;
                    break;
                }
                iter--;
            }
        }
        return resp;
    }
}
