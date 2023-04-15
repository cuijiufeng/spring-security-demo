package io.inferiority.demo.springsecurity.service.impl;

import io.inferiority.demo.springsecurity.model.User;
import io.inferiority.demo.springsecurity.service.IAuthService;
import io.inferiority.demo.springsecurity.utils.JwtUtil;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Duration;

/**
 * @author cuijiufeng
 * @Class AuthLoginServiceImpl
 * @Date 2023/4/14 17:45
 */
@Service
public class AuthServiceImpl implements IAuthService {
    @Value("${token.header:Authorization}")
    private String tokenHeader;
    @Value("#{T(org.springframework.boot.convert.DurationStyle).detectAndParse('${token.duration:5m}')}")
    private Duration tokenDuration;
    @Value("${jwt.priv.key:308204bd020100300d06092a864886f70d0101010500048204a7308204a30201000282010100818c52631dd2184245856be46c65dfb830c2653278b9ffe518370a1dbdd59d66bea16ee17e94f13c664e32621ac041dca0da5c41b71919e3471b597376de0bdbed5122ef924a6701de0661a8b833e86fb5d7bf6156a3295223b8d510ba3e76f8232613ec23a6bd6047294147e06f9db4197cd35567abd9d8caa213e1376754cdf018a2f701803f1318170b9850bcf5547e45271d9efe00c830b0c27f4029921adee4f826df2b8a79c1f04c802e64979a92de8db4bb7fffa4589730a1598b5c151abecb38cf27a6b71cbe7da6dfd6f10ba3118f70abf4360851200a55030c85578b7bd33cbbb25365003f72a730a919e2f61ec9efc3a814c640377009e6a698370203010001028201005284a041f44fcc1602948efa0c3604001e46e510f6277c216080469855404927960bdc46144c293150f544508b3ba578349982a4754daed8bf856340121a34319d83ce1e0df00896fee6cfb419c92ea8618b10a39efd0528060b08e4c3cd601884c30f739dedeab8f5fb08531159e7793db6ee227b8bd292add7f993bea7ba331ee37a853b8d3b3517d75ec76bf3469a9d44a0be8ced981e3486493a292100fca3662355890df2dbf9c652affaa1f5a85325edc1871f602e2076c1ad210979ad34b1d89c4c093e35db415fc6cb04d374cc5c149709fce3f4abaaae5017279b8f64ad9bbdda7b3eb5d0503cb76388294ae28ad023f2ccec0c866fb9b0cfc80c6102818100c1f1cdd77af5a566eb35b1e7a3b5d971c55597ab4b13eb5464c9ae370b5eb14cbad7273eaf84770aa9d807145143fc098f4ccd85c992c353c4ccce1f60fc12c7676350abc29a6cd1c04a67123b5f21807a917e54f36212dc6603e9123e93a72dde21a14845c4d6f726c8da064d8502b504573f5d057c490902b5ccffb22362c702818100aaffbddbbb0b769f9d9f77254ae8fb9ae00a83315cc66159596e51f41d97a401050345138d76d6a31987296cbd1f5491b090960cedcff9c9607cadecef6cbcdda231363bff9271812c0f8bd066046b52aac1333a86e786b8ff8a86dbde2cd867871dc9a83b907943aeb515fda089f9fb84a5d9f8c678174202ebde6f8901af11028181009c794574e37c8afd1529d626285c105a8d36784eb81d13119cdb66d67c32e2e3201f74474bfe98b89ebd6f40c22c128d77f324221bb6702706b1a25f88b4f745b0315da5a6354734976aafebf2c0d6dbbc87e4200fd90194962df37e47c482ee2bb880523a100ccb8c9c21e351e9cd7c544b3c1105daf765f0acd2ff3d2ef58302818034954a13dcb3ccb98ea5efe486e198256b09b0882868549d3e33ff7d9aeffb9dd8a5c5c31a7855ce77d62975e7be4faa316a74c9eb034e638f89278eb99d6d8296129e03db367432ed92a642e26c2f788f4bb3ee2677e1e4d7a8088a6a0ace4d1fae9849646fabea9539d85652dcbb038e29d61a7b2588b962de975b22fd62910281807ee1f14dbd40b0cb2d6408c2ef38e231355ae4ab65ee2fc89d163c5b41573d943752da6dc50dfd43c57f79d2c23c9bb34e056b07e85666e4d55eb2e030da66b72e45026915b4195a7aa3c79d71831a2fd1581d7e5bb7030894de591810bc458c746c9b68e7ebe49cb2a9a995c1b2cebc6cc325f910920365f7bb24fcabc08dfc}")
    private String jwtPrivKey;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        //将用户存入上下文中
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        //token
        PrivateKey privateKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Hex.decodeHex(jwtPrivKey)));
        } catch (Exception e) {
            throw new RuntimeException("parse jwt key error");
        }
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        // TODO: 2023/4/15 密码不放进token中
        response.setHeader(tokenHeader, JwtUtil.createJwt(privateKey, authenticate.getPrincipal(), tokenDuration.toMillis()));
    }

    @Override
    public void logout(User user) {
        //清除上下文
        SecurityContextHolder.clearContext();
    }
}
