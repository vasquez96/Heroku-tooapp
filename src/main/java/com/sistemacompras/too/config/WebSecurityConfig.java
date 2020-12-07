package com.sistemacompras.too.config;

import com.sistemacompras.too.component.CustomSuccessHandler;
import com.sistemacompras.too.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    //Necesario para evitar que la seguridad se aplique a los resources
    //Como los css, imagenes y javascripts
    String[] resources = new String[]{
            "/include/**",
            "/css/**",
            "/icons/**",
            "/img/**",
            "/js/**",
            "/vendor/**",
            "/scss/**"
    };
    
    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new CustomSuccessHandler();
    }
    

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()//Quienes están autorizados
                .antMatchers(resources).permitAll()
                .antMatchers("/", "/login").permitAll()
                .antMatchers("/admin/**").access("hasRole('ADMIN')")
                .antMatchers("/proveedor/**").access("hasRole('PROVEEDOR')")
                .antMatchers("/jefe/**").access("hasRole('JEFE')")
                .antMatchers("/empleado/**").access("hasRole('EMPLEADO')")
                .antMatchers("/bodega/**").access("hasRole('BODEGUERO')")
                .anyRequest().authenticated() //Cualquier otra ruta que no está arriba debe ser autenticada
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
//                .defaultSuccessUrl("/menu")
                .successHandler(myAuthenticationSuccessHandler()) //Login exitoso
                .failureUrl("/login?error=true") //Fallo el login
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
                .logout() //Quien se puede desloguear
                .permitAll() //Todos los usuarios
                .logoutSuccessUrl("/login?logout");
    }

    BCryptPasswordEncoder bCryptPasswordEncoder;

    //Crea el encriptador de contraseñas
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        bCryptPasswordEncoder = new BCryptPasswordEncoder(4);
        //El numero 4 representa que tan fuerte quieres la encriptacion.
        //Se puede en un rango entre 4 y 31.
        //Si no pones un numero el programa utilizara uno aleatoriamente cada vez
        //que inicies la aplicacion, por lo cual tus contrasenas encriptadas no funcionaran bien
        return bCryptPasswordEncoder;
    }

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    //Registra el service para usuarios y el encriptador de contrasena
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        // Setting Service to find User in the database.
        // And Setting PassswordEncoder
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    
}
