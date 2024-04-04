package rivermonitoringservice.serial;

public class SerialParser {
    private StringBuffer stringBuffer;

    public SerialParser() {
        this.stringBuffer = new StringBuffer();
    }

    public String parseReceivedMessage(final String receivedMessage) {
        String result = receivedMessage;
        result.replaceAll("\r", "");
        this.stringBuffer.append(result);
        boolean goAhead = true;
        while (goAhead) {
            String msg2 = this.stringBuffer.toString();
            int index = msg2.indexOf("\n");
            if (index >= 0) {
                result = msg2.substring(0, index);
                this.stringBuffer = new StringBuffer("");
                if (index + 1 < msg2.length()) {
                    this.stringBuffer.append(msg2.substring(index + 1));
                }
            } else {
                goAhead = false;
            }
        }
        return result;
    }
}
