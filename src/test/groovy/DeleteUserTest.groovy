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
class DeleteUserTest extends Specification {
    @Shared
    RESTClient restClient = new RESTClient("http://localhost:8081")

    def "Check Delete User Controller"() {
        when:
        PhoneDTO phoneDTO = new PhoneDTO("22223333", "11", "54")
        List<PhoneDTO> listPhoneDto = new ArrayList<>();
        listPhoneDto.add(phoneDTO)
        UserDTO userDTO = new UserDTO("Juan Delete", "juandelete@gmail.com", "Juan88", listPhoneDto);

        def createResponse = restClient.post([path: "/api/users", contentType: "application/json", body:userDTO])
        def uid = createResponse.data.id;
        def response = restClient.delete([path: "/api/users/"+uid, contentType: "application/json"]);

        then:
        response.status == 200
    }

    def "Check Delete User not found"() {
        when:
        def uid = "newuser";
        def response = restClient.delete([path: "/api/users/"+uid, contentType: "application/json"]);

        then:
        HttpResponseException e = thrown(HttpResponseException)
        assert e.response.status == 404
    }
}
