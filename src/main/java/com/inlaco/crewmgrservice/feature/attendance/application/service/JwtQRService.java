// package com.inlaco.crewmgr.attendance.infrastructure.service.qr;
// import com.inlaco.crewmgr.attendance.domain.exception.InvalidQrTokenException;
// import com.inlaco.crewmgr.attendance.domain.model.QrData;
// import com.inlaco.crewmgr.attendance.domain.service.qr.QrService;
// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
// import java.util.Date;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// @Service
// public class JwtQrService implements QrService {
//     @Value("${jwt.secret:defaultSecretKey}") // Use a default secret key if not configured
//     private String jwtSecret;
//     @Value("${jwt.expirationMs:3600000}") // Default expiration 1 hour
//     private long jwtExpirationMs;
//     @Override
//     public QrData verify(String token) {
//         try {
//             Claims claims = Jwts.parser()
//                 .setSigningKey(jwtSecret)
//                 .parseClaimsJws(token)
//                 .getBody();
//             String personId = claims.get("personId", String.class);
//             String scheduleId = claims.get("scheduleId", String.class);
//             String companyId = claims.get("companyId", String.class);
//             if (personId == null || scheduleId == null || companyId == null) {
//                 throw new InvalidQrTokenException(
//                     "QR token is missing required claims (personId, scheduleId, companyId)."
//                 );
//             }
//             // Optional: Add expiration check if not already handled by JWT library implicitly
//             if (claims.getExpiration().before(new Date())) {
//                 throw new InvalidQrTokenException("QR token has expired.");
//             }
//             return new QrData(personId, scheduleId, companyId);
//         } catch (Exception e) {
//             throw new InvalidQrTokenException(
//                 "Invalid QR token: " + e.getMessage()
//             );
//         }
//     }
//     // Method to generate a QR token (useful for testing or other services)
//     public String generateToken(
//         String personId,
//         String scheduleId,
//         String companyId
//     ) {
//         Date now = new Date();
//         Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
//         return Jwts.builder()
//             .claim("personId", personId)
//             .claim("scheduleId", scheduleId)
//             .claim("companyId", companyId)
//             .setIssuedAt(now)
//             .setExpiration(expiryDate)
//             .signWith(SignatureAlgorithm.HS512, jwtSecret)
//             .compact();
//     }
// }
