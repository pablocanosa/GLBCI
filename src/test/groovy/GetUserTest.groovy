import com.glbci.eval.model.dto.PhoneDTO
import com.glbci.eval.model.dto.UserDTO
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Shared
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles(value = "test")
@ContextConfiguration
class GetUserTest extends Specification{
    @Shared
    RESTClient restClient = new RESTClient("http://localhost:8081")

    def "Check Get User Controller"() {
        when:
        PhoneDTO phoneDTO = new PhoneDTO("22223333", "11", "54")
        List<PhoneDTO> listPhoneDto = new ArrayList<>();
        listPhoneDto.add(phoneDTO)
        UserDTO userDTO = new UserDTO("Juan Manual", "juanmanuel@gmail.com", "Juan88", listPhoneDto);

        def createResponse = restClient.post([path: "/api/users", contentType: "application/json", body:userDTO])
        def uid = createResponse.data.id;
        def getResponse = restClient.get([path: "/api/users/"+uid, contentType: "application/json"]);

        then:
        getResponse.status == 200
    }

    def "Check User not found"() {
        when:
        def uid = "newuser";
        def getResponse = restClient.get([path: "/api/users/"+uid, contentType: "application/json"]);

        then:
        HttpResponseException e = thrown(HttpResponseException)
        e.response.status == 404
    }
}
