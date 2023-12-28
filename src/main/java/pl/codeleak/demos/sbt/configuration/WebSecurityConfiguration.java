package pl.codeleak.demos.sbt.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import pl.codeleak.demos.sbt.service.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MyUserDetailsService userDetailsService;



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String loginPage = "/login";
        String logoutPage = "/logout";

        http.cors().and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(loginPage).permitAll()
                .antMatchers("/registration").permitAll()
                .antMatchers("/loginrest/in/**").permitAll()
                .antMatchers("/admin/**").hasAnyAuthority("VENDER","ADMIN","VENDERPRO","CLIENTECC")
                .antMatchers("/cierrecajapublico/**").hasAnyAuthority("VENDER","ADMIN","VENDERPRO")
                .antMatchers("/productos/**").hasAnyAuthority("ADMIN","VENDERPRO")
                .antMatchers("/cuentascorrientes/**").hasAnyAuthority("VENDER","ADMIN","VENDERPRO")
                .antMatchers("/pagoproveedores/**").hasAnyAuthority("VENDER","ADMIN","VENDERPRO")
                .antMatchers("/proveedores/**").hasAnyAuthority("ADMIN","VENDERPRO")
                .antMatchers("/egresoscajachica/**").hasAnyAuthority("VENDER","ADMIN","VENDERPRO").anyRequest()
                .authenticated()
                .and().csrf().disable()
                .formLogin()
                .loginPage(loginPage)
                .loginPage("/")
                .failureUrl("/login?error=true")
                //.defaultSuccessUrl("/admin/home")
                .defaultSuccessUrl("/admin/home")
                .usernameParameter("user_name")
                .passwordParameter("password")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher(logoutPage))
                .logoutSuccessUrl(loginPage).and().exceptionHandling();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:8100"); // Permite solicitudes desde este origen
        config.addAllowedMethod("*"); // Permite todos los métodos
        config.addAllowedHeader("*"); // Permite todos los encabezados
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
