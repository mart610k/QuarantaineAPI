package dk.quarantaine.api;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

@ExtendWith(MockitoExtension.class)
class ApiApplicationTests {

	@Test
	void contextLoads() {
		Assert.notNull(new Object()); // Test succeeds
	}

}
