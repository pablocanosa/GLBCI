import com.glbci.eval.exceptions.AlreadyExistsException
import com.glbci.eval.model.dto.PhoneDTO
import com.glbci.eval.model.dto.UserDTO
import groovyx.net.http.HttpResponseException
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Shared
import spock.lang.Specification
import groovyx.net.http.RESTClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles(value = "test")
@ContextConfiguration
class CreateUserTest extends Specification {
    @Shared
    RESTClient restClient = new RESTClient("http://localhost:8081")

    def "Check create User Controller"() {
        when:
        PhoneDTO phoneDTO = new PhoneDTO("22223333", "11", "54")
        List<PhoneDTO> listPhoneDto = new ArrayList<>();
        listPhoneDto.add(phoneDTO)
        UserDTO userDTO = new UserDTO("Juan Carlos", "juancarlos@gmail.com", "Juan88", listPhoneDto);

        def response = restClient.post([path: "/api/users", contentType: "application/json", body:userDTO])

        then:
        response.status == 201
    }

    def "Check create User with existing email"() {
        when:
        PhoneDTO phoneDTO = new PhoneDTO("22223333", "11", "54")
        List<PhoneDTO> listPhoneDto = new ArrayList<>();
        listPhoneDto.add(phoneDTO)
        UserDTO userDTO = new UserDTO("Juan Pablo", "juanpablo@gmail.com", "Juan88", listPhoneDto);

        def response = restClient.post([path: "/api/users", contentType: "application/json", body:userDTO])
        response = restClient.post([path: "/api/users", contentType: "application/json", body:userDTO])

        then:
        HttpResponseException e = thrown(HttpResponseException)
        e.response.status == 409
    }

    def "Check create User with wrong email format"() {
        when:
        PhoneDTO phoneDTO = new PhoneDTO("22223333", "11", "54")
        List<PhoneDTO> listPhoneDto = new ArrayList<>();
        listPhoneDto.add(phoneDTO)
        UserDTO userDTO = new UserDTO("Juan Carlos", "juancarlosgmail.com", "Juan88", listPhoneDto);

        def response = restClient.post([path: "/api/users", contentType: "application/json", body:userDTO])

        then:
        HttpResponseException e = thrown(HttpResponseException)
        e.response.status == 400
    }

    def "Check create User with wrong password format"() {
        when:
        PhoneDTO phoneDTO = new PhoneDTO("22223333", "11", "54")
        List<PhoneDTO> listPhoneDto = new ArrayList<>();
        listPhoneDto.add(phoneDTO)
        UserDTO userDTO = new UserDTO("Juan Carlos", "juancarlos1@gmail.com", "hola1", listPhoneDto);

        def response = restClient.post([path: "/api/users", contentType: "application/json", body:userDTO])

        then:
        HttpResponseException e = thrown(HttpResponseException)
        e.response.status == 400
    }
}
