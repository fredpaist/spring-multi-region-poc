package ee.fredpaist.multiregion.service;

import ee.fredpaist.multiregion.data_accesors.common.system.ConfigurationType;
import ee.fredpaist.multiregion.data_accesors.common.system.SystemConfiguration;
import ee.fredpaist.multiregion.data_accesors.common.system.SystemConfigurationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SystemConfigurationProviderTest {

    @Mock
    private SystemConfigurationRepository repository;

    @Test
    void test_returns_system_configuration_when_configuration_type_exists() {
        SystemConfigurationProvider provider = new SystemConfigurationProvider(repository);
        ConfigurationType type = ConfigurationType.PERSON_EMAIL_MANDATORY;
        SystemConfiguration expectedConfig = new SystemConfiguration().setName(type).setValue(true);

        when(repository.findSystemConfigurationByName(type)).thenReturn(expectedConfig);

        SystemConfiguration result = provider.getConfigurationByType(type);

        assertNotNull(result);
        assertEquals(expectedConfig, result);
    }

    @Test
    void test_correctly_maps_configuration_type_to_system_configuration() {
        SystemConfigurationProvider provider = new SystemConfigurationProvider(repository);
        ConfigurationType type = ConfigurationType.PERSON_PHONE_MANDATORY;
        SystemConfiguration expectedConfig = new SystemConfiguration().setName(type).setValue(false);

        when(repository.findSystemConfigurationByName(type)).thenReturn(expectedConfig);

        SystemConfiguration result = provider.getConfigurationByType(type);

        assertNotNull(result);
        assertEquals(type, result.getName());
    }

    @Test
    void test_throws_runtime_exception_when_configuration_type_does_not_exist() {
        SystemConfigurationProvider provider = new SystemConfigurationProvider(repository);
        ConfigurationType type = ConfigurationType.PERSON_EMAIL_MANDATORY;

        when(repository.findSystemConfigurationByName(type)).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            provider.getConfigurationByType(type);
        });

        assertEquals("System configuration not specified", exception.getMessage());
    }

}