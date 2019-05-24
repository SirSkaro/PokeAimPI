package skaro.pokeaimpi.web;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import skaro.pokeaimpi.services.PointsService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(PointsController.class)
public class PointsControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
	private PointsService pointsService;
	
	@Before
	public void setUp() {
	}
	
}
