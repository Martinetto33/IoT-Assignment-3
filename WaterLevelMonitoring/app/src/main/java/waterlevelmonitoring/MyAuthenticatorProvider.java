package waterlevelmonitoring;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.annotations.Nullable;
import com.hivemq.extension.sdk.api.auth.Authenticator;
import com.hivemq.extension.sdk.api.auth.parameter.AuthenticatorProviderInput;
import com.hivemq.extension.sdk.api.services.auth.provider.AuthenticatorProvider;

import java.util.Map;
import java.nio.charset.StandardCharsets;

public class MyAuthenticatorProvider implements AuthenticatorProvider {
    private static final String espClientUsername = "esiot-2024-group-4-esp";
    private static final String waterLevelMonitoringClientUsername = "esiot-2024-group-4-wmlc";

    @Override
    public @Nullable Authenticator getAuthenticator(@NotNull AuthenticatorProviderInput authenticatorProviderInput) {
        return new MySimpleAuthenticator(
            Map.of(MyAuthenticatorProvider.espClientUsername, 
                   this.convertToUTF_8("espg4esiot24"),
                   MyAuthenticatorProvider.waterLevelMonitoringClientUsername,
                   this.convertToUTF_8("wlmc4esiot24"))
        );
    }

    /**
     * MQTT communications use UTF_8 charsets, so passwords need to be memorised in this format.
     * @param inputString The String to be converted to UTF_8.
     * @return The converted String.
     */
    private String convertToUTF_8(String inputString) {
        return new String(inputString.getBytes(), StandardCharsets.UTF_8);
    }
    
}
