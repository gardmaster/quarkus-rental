package master.gard.utils;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
public class SecurityUtils {

    private final SecurityIdentity identity;
    private final JsonWebToken jsonWebToken;

    public SecurityUtils(SecurityIdentity identity, JsonWebToken jsonWebToken) {
        this.identity = identity;
        this.jsonWebToken = jsonWebToken;
    }

    public String getUserUuid() {
        String sub = jsonWebToken.getClaim("sub");
        if (sub != null) {
            return sub;
        }
        throw new IllegalStateException("JWT token does not contain 'sub' claim");
    }

    public boolean hasRole(String role) {
        return identity.hasRole(role);
    }
}
