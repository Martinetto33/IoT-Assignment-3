package waterlevelmonitoring;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.auth.SimpleAuthenticator;
import com.hivemq.extension.sdk.api.auth.parameter.SimpleAuthInput;
import com.hivemq.extension.sdk.api.auth.parameter.SimpleAuthOutput;
import com.hivemq.extension.sdk.api.packets.connect.ConnackReasonCode;
import com.hivemq.extension.sdk.api.packets.connect.ConnectPacket;

import java.util.Map;
import java.util.Optional;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * A HiveMQ {@link com.hivemq.extension.sdk.api.auth.SimpleAuthenticator}, used to authenticate only
 * known MQTT clients.
 * 
 * Source code was taken from here: 
 * https://docs.hivemq.com/hivemq/latest/extensions/registries.html#security-registry;
 */
public class MySimpleAuthenticator implements SimpleAuthenticator {

    private final Map<String, String> usernamePasswordMap;

    /**
     * Builds a new authenticator given an input map of ursernames-passwords.
     * @param usernamePasswordMap the provided map of usernames and passwords.
     */
    public MySimpleAuthenticator(final Map<String, String> usernamePasswordMap) {
        this.usernamePasswordMap = usernamePasswordMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConnect(@NotNull SimpleAuthInput simpleAuthInput, @NotNull SimpleAuthOutput simpleAuthOutput) {
        // get connect packet from input object
        final ConnectPacket connectPacket = simpleAuthInput.getConnectPacket();

        // validate the username and password combination
        if (this.validate(connectPacket.getUserName(), connectPacket.getPassword())) {
            simpleAuthOutput.authenticateSuccessfully();
            System.out.println("Accepted connection of client: " + connectPacket.getUserName().get());
        } else {
            simpleAuthOutput.failAuthentication(ConnackReasonCode.BAD_USER_NAME_OR_PASSWORD, "Wrong username or password.");
            System.out.println("Refused one connection due to wrong or missing credentials!");
        }
    }

    private boolean validate(Optional<String> usernameOptional, Optional<ByteBuffer> passwordOptional) {
        // if no username or password is set, validation fails
        if (!usernameOptional.isPresent() || !passwordOptional.isPresent()) {
            return false;
        }
        final String username = usernameOptional.get();
        final byte[] bytes = this.getBytesFromBuffer(passwordOptional.get());
        final String password = new String(bytes, StandardCharsets.UTF_8);
        // get password for username from map
        final String passwordFromMap = usernamePasswordMap.get(username);
        // return true if passwords are equal, else false
        return password.equals(passwordFromMap);
    }

    private byte[] getBytesFromBuffer(ByteBuffer byteBuffer) {
        final byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        return bytes;
    }
}