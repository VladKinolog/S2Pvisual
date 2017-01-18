package sample;


import static java.lang.Integer.parseInt;

/**
 * Получение строки запроса по протоколу MB Ascii
 */
public class MbAsciiStringRequest {

    public final static int FUNCTION_01 = 1;
    public final static int FUNCTION_02 = 2;
    public final static int FUNCTION_03 = 3;
    public final static int FUNCTION_04 = 4;
    public final static int FUNCTION_05 = 5;
    public final static int FUNCTION_06 = 6;

    private String request;
    private int deviceAddress;
    private int mbFunction;
    private int startRegister;
    private int quantityGetReg;
    private String LrcCheckSum;


    MbAsciiStringRequest () {};

    /**
     * Конструктор построения строки запроса к устройству по протоколу MB ASCII
     * @param request Строка MB ASCII формата ПРИМЕР :01030005001ED9 + "\r" + "\n";
     */
    MbAsciiStringRequest (String request) {
       this.request = request;
    }

    /**
     * Конструктор построения строки запроса к устройству
     * @param deviceAddress Адресс устройства в сети ModBus
     * @param mbFunction Функция (команда) MB
     * @param startRegister Начальный регистр
     * @param quantityGetReg Количество регистров для чтения
     */

    MbAsciiStringRequest (int deviceAddress, int mbFunction, int startRegister, int quantityGetReg) {
        String request = "";
        this.deviceAddress = deviceAddress;
        this.mbFunction = mbFunction;
        this.startRegister = startRegister;
        this.quantityGetReg = quantityGetReg;
        request = ":"
                + String.format("%02X" ,deviceAddress)
                + String.format("%02X", mbFunction)
                + String.format("%04X", startRegister)
                + String.format("%04X", quantityGetReg);
        this.LrcCheckSum = getLrcCheckSum(request);
        this.request = request + LrcCheckSum  + "\r" + "\n";

    }

    private String getLrcCheckSum (String request) {
        String str ;
        char [] respCharArray;
        respCharArray = request.toCharArray();
        char  chHigh;
        char chLow;
        Integer getValue;
        Integer value = 0;

        if (respCharArray.length == 0)
            throw new IllegalArgumentException("response string is not initialized");

        for (int i = 1; i < respCharArray.length; i=i+2){
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
     * Получение строки отправки по модбас ASCII исходя из параметров,
     * @return стандартная строка ModBus ASCII.
     */
    public String getRequest() {
        return request;
    }

    public void setRequest (String request) {
        this.request = request;
    }

    public int getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(int deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public int getMbFunction() {
        return mbFunction;
    }

    public void setMbFunction(int mbFunction) {
        this.mbFunction = mbFunction;
    }

    public int getStartRegister() {
        return startRegister;
    }

    public void setStartRegister(int startRegister) {
        this.startRegister = startRegister;
    }

    public int getQuantityGetReg() {
        return quantityGetReg;
    }

    public void setQuantityGetReg(int quantityGetReg) {
        this.quantityGetReg = quantityGetReg;
    }

    public String getLrcCheckSum() {
        return LrcCheckSum;
    }

    public void setLrcCheckSum(String lrcCheckSum) {
        LrcCheckSum = lrcCheckSum;
    }
}
