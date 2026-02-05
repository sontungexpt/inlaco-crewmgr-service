package com.inlaco.crewmgrservice.infrastructure.security.jwt.crypto;

import com.inlaco.crewmgrservice.infrastructure.security.jwt.config.JwtProperties;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtKeyProvider {

  private final JwtProperties props;

  public JwtKeyProvider(JwtProperties props) {
    this.props = props;
  }

  public SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(props.getSecretKey());
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
