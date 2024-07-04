package ee.fredpaist.multiregion.service;

import ee.fredpaist.multiregion.data_accesors.common.system.ConfigurationType;
import ee.fredpaist.multiregion.data_accesors.common.system.SystemConfiguration;
import ee.fredpaist.multiregion.data_accesors.common.system.SystemConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SystemConfigurationProvider {

    private final SystemConfigurationRepository repository;

    public SystemConfiguration getConfigurationByType(ConfigurationType type) {
        return Optional.ofNullable(repository.findSystemConfigurationByName(type))
                .orElseThrow(() -> new RuntimeException("System configuration not specified"));
    }
}
