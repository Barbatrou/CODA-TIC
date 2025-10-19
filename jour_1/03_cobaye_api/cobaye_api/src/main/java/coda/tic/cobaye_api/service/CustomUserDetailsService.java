package coda.tic.cobaye_api.service;

import coda.tic.cobaye_api.model.User;
import coda.tic.cobaye_api.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        List<GrantedAuthority> authorities = user.getPermissions().stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        // No password needed for simple username-only auth
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getName())
            .password("")
            .authorities(authorities)
            .build();
    }
}
