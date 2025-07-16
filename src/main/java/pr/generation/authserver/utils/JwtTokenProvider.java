//package pr.generation.authserver.utils;
//
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.security.KeyFactory;
//import java.security.PrivateKey;
//import java.security.PublicKey;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.Base64;
//import java.util.Date;
//import java.util.List;
//
//@Component
//public class JwtTokenProvider {
//
//    @Value("${jwt.issuer}")
//    private String issuer;
//
//    private final PrivateKey privateKey;
//    private final PublicKey publicKey;
//
//    public JwtTokenProvider() throws Exception {
//        this.privateKey = loadPrivateKey("private_key.pem");
//        this.publicKey = loadPublicKey("public_key.pem");
//    }
//
//    public String generateToken(String userId, List<String> roles) {
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + 86400000); // 24시간
//
//        return Jwts.builder()
//                .subject(userId)
//                .issuer(issuer)
//                .claim("roles", roles)
//                .claim("test", "test")
//                .issuedAt(now)
//                .expiration(expiryDate)
//                .signWith(privateKey, Jwts.SIG.RS512)
//                .compact();
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parser()
//                    .verifyWith(publicKey)
//                    .build()
//                    .parseSignedClaims(token);
//            return true;
//        } catch (JwtException | IllegalArgumentException e) {
//            return false;
//        }
//    }
//
//    private PrivateKey loadPrivateKey(String filename) throws Exception {
//        String key = Files.readString(Paths.get(filename))
//                .replace("-----BEGIN PRIVATE KEY-----", "")
//                .replace("-----END PRIVATE KEY-----", "")
//                .replaceAll("\\s", "");
//
//        byte[] decoded = Base64.getDecoder().decode(key);
//        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
//        KeyFactory kf = KeyFactory.getInstance("RSA");
//        return kf.generatePrivate(spec);
//    }
//
//    private PublicKey loadPublicKey(String filename) throws Exception {
//        String key = Files.readString(Paths.get(filename))
//                .replace("-----BEGIN PUBLIC KEY-----", "")
//                .replace("-----END PUBLIC KEY-----", "")
//                .replaceAll("\\s", "");
//
//        byte[] decoded = Base64.getDecoder().decode(key);
//        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
//        KeyFactory kf = KeyFactory.getInstance("RSA");
//        return kf.generatePublic(spec);
//    }
//}
