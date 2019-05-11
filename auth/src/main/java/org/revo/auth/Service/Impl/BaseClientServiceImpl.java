package org.revo.auth.Service.Impl;

import org.revo.auth.Repository.BaseClientRepository;
import org.revo.auth.Service.BaseClientService;
import org.revo.auth.Service.UserService;
import org.revo.core.base.Domain.BaseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BaseClientServiceImpl implements BaseClientService {
    @Autowired
    private BaseClientRepository baseClientRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Optional<BaseClient> loadClientByClientId(String clientId) {
        return baseClientRepository.findByClientId(clientId);
    }

    @Override
    public void save(BaseClient baseClient) {
        if (userService.current() != null) baseClient.setUserId(userService.current());
        baseClientRepository.save(baseClient);
    }

    @Override
    public long count() {
        return baseClientRepository.count();
    }

    @Override
    public List<BaseClient> findAll(String id) {
        return baseClientRepository.findByUserId(id);
    }

    @Override
    public void delete(String id) {
        this.baseClientRepository.delete(new BaseClient(id));
    }
}
