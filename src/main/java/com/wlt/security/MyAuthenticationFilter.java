package com.wlt.security;

import com.wlt.model.User;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final String Myusername = "admin";
    private static final String Mypassword = "admin";

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println(" ---------------  MyAuthenticationFilter attemptAuthentication--------------- ");
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            //获取表单中的用户名和密码。
            String username = this.obtainUsername(request);
            String password = this.obtainPassword(request);
            if (username == null) {
                username = "";
            }

            if (password == null) {
                password = "";
            }

            username = username.trim();
            password = password.trim();
            /*
            * 相当于从数据库中取数据查询一个user
            * */
            User users=new User();
            users.setPasswor(Mypassword);
            users.setUsename(Myusername);
            /*
            * 进行密码和用户名的验证
            * */
            if (users == null || !users.getPasswor().equals(password)) {
                // 在界面输出自定义的信息！！
                throw new BadCredentialsException("用户名或密码不匹配！");
            }
            // 当验证都通过后，把用户信息放在session里
            request.getSession().setAttribute("userSession", users);
            // 记录登录信息

            System.out.println("userId----" + users.getId()+ "---ip--"
                    + request.getLocalAddr());
            // 运行UserDetailsService的loadUserByUsername 再次封装Authentication
            // 允许子类设置详细属性
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            this.setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);

        }
    }
}