package com.ryadovoy.linkify.service.impl;

import com.ryadovoy.linkify.dto.projection.UserSummary;
import com.ryadovoy.linkify.dto.request.RegistrationRequest;
import com.ryadovoy.linkify.exception.RoleNotFoundException;
import com.ryadovoy.linkify.model.Role;
import com.ryadovoy.linkify.model.RoleName;
import com.ryadovoy.linkify.model.User;
import com.ryadovoy.linkify.exception.UserAlreadyExistsException;
import com.ryadovoy.linkify.repository.RoleRepository;
import com.ryadovoy.linkify.repository.UserRepository;
import com.ryadovoy.linkify.service.UserService;
import com.ryadovoy.linkify.util.PageRequestFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(RegistrationRequest registrationRequest) {
        String email = registrationRequest.getEmail();

        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("User already exists with email: " + email);
        }

        String password = registrationRequest.getPassword();
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(email, encodedPassword);

        Role role = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new RoleNotFoundException("USER role was not found"));

        user.getRoles().add(role);
        userRepository.save(user);
        log.info("New user registered: " + user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserSummary> findAllUsersWithRoles(int page, int size) {
        Sort sort = Sort.by("id");
        PageRequest pageRequest = PageRequestFactory.createRequest(page, size, sort);

        Page<Long> userIds = userRepository.findAllIds(pageRequest);
        List<UserSummary> userSummaries = userRepository.findWithRolesByIdIn(userIds.getContent(), sort);

        return new PageImpl<>(userSummaries, pageRequest, userIds.getTotalElements());
    }
}
