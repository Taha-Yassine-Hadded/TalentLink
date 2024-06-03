package com.platform.platformOffreM.security;


import com.platform.platformOffreM.entities.User;
import com.platform.platformOffreM.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collections;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    UserRepository userRepository;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userRepository.findByUsername(username);
                if (user == null) {
                    throw new UsernameNotFoundException("Invalid username or password.");
                }
                if (!user.isEnabled()) {
                    throw new DisabledException("User account is disabled.");
                }
                return new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getRoleName())));
            }
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();



        http
                .authorizeRequests()


                .antMatchers("/admins/**").hasAuthority("ADMIN")


                .antMatchers("/offres/").hasAnyAuthority("ENTREPRISE", "ADMIN", "TALENT")
                .antMatchers("/offres/add").hasAuthority("ENTREPRISE")
                .antMatchers("/offres/save").hasAuthority("ENTREPRISE")
                .antMatchers("/offres/saveCompetence").hasAuthority("ENTREPRISE")
                .antMatchers("/offres/newCompetence").hasAuthority("ENTREPRISE")
                .antMatchers("/offres/subscribe**").hasAuthority("TALENT")
                .antMatchers("/offres/cancelSubscribe**").hasAuthority("TALENT")
                .antMatchers("/offres/delete**").hasAnyAuthority("ENTREPRISE","ADMIN")
                .antMatchers("/offres/deleteCandidature**").hasAuthority("ENTREPRISE")
                .antMatchers("/offres/offreInfo**").hasAnyAuthority("ENTREPRISE", "ADMIN", "TALENT")
                .antMatchers("/offres/acceptCandidature**").hasAuthority("ENTREPRISE")
                .antMatchers("/offres/rejectCandidature**").hasAuthority("ENTREPRISE")
                .antMatchers("/offres/edit**").hasAuthority("ENTREPRISE")
                .antMatchers("/offres/saveOffreChange/").hasAuthority("ENTREPRISE")
                .antMatchers("/offres/deleteCompetence**").hasAuthority("ENTREPRISE")
                .antMatchers("/offres/deleteOldCompetence**").hasAuthority("ENTREPRISE")
                .antMatchers("/offres/deleteNewCompetence**").hasAuthority("ENTREPRISE")



                .antMatchers("/notifications").hasAnyAuthority("ENTREPRISE", "TALENT")


                .antMatchers("/profils/myProfilT").hasAuthority("TALENT")
                .antMatchers("/profils/myProfilE").hasAuthority("ENTREPRISE")
                .antMatchers("/profils/talentProfil**").hasAnyAuthority("ENTREPRISE", "ADMIN", "TALENT")
                .antMatchers("/profils/entrepriseProfil**").hasAnyAuthority("ENTREPRISE", "ADMIN", "TALENT")
                .antMatchers("/profils/follow**").hasAuthority("TALENT")
                .antMatchers("/profils/unfollow**").hasAuthority("TALENT")
                .antMatchers("/profils/updateTalent").hasAuthority("TALENT")
                .antMatchers("/profils/updateEntreprise").hasAuthority("ENTREPRISE")
                .antMatchers("/profils/saveTalentUpdates").hasAuthority("TALENT")
                .antMatchers("/profils/saveEntrepriseUpdates").hasAuthority("ENTREPRISE")
                .antMatchers("/profils/deleteProfilPicture**").hasAuthority("TALENT")


                .antMatchers("/cv/cvInfo").hasAnyAuthority("ENTREPRISE", "ADMIN", "TALENT")
                .antMatchers("/cv/download**").hasAnyAuthority("ENTREPRISE", "ADMIN", "TALENT")
                .antMatchers("/cv/getCvPicture**").hasAnyAuthority("ENTREPRISE", "ADMIN", "TALENT")

                .antMatchers("/cv/**").hasAuthority("TALENT")


                .anyRequest()
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"));


        http
                .exceptionHandling()
                .accessDeniedPage("/403");
    }


}
