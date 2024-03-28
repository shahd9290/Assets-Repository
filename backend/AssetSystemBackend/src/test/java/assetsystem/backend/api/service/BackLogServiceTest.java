package assetsystem.backend.api.service;

import assetsystem.backend.api.model.BackLog;
import assetsystem.backend.api.repository.BackLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BackLogServiceTest {

    @Mock
    BackLogRepository backLogRepository;

    @InjectMocks
    BackLogService backLogService;

    @Test
    void testGetBackLogs() {
        // Create mock list of BackLogs
        List<BackLog> backLogs = new ArrayList<>();
        backLogs.add(new BackLog());
        backLogs.add(new BackLog());
        backLogs.add(new BackLog());

        // Simulate backLogRepository.findAll() to return the mock list
        when(backLogRepository.findAll()).thenReturn(backLogs);

        List<BackLog> result = backLogService.getBackLogs();

        // Verify that the method behaves correctly
        assertEquals(3, result.size());
        verify(backLogRepository, times(1)).findAll();
    }
}
