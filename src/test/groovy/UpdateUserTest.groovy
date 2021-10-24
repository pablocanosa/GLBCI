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
class UpdateUserTest extends Specification {
    @Shared
    RESTClient restClient = new RESTClient("http://localhost:8081")

    def "Check Update User Controller"() {
        when:
        PhoneDTO phoneDTO = new PhoneDTO("22223333", "11", "54")
        List<PhoneDTO> listPhoneDto = new ArrayList<>();
        listPhoneDto.add(phoneDTO)
        UserDTO userDTO = new UserDTO("Jose Update", "joseupdate@gmail.com", "Jose88", listPhoneDto);

        def response = restClient.post([path: "/api/users", contentType: "application/json", body:userDTO])
        userDTO.setId(response.data.id)
        userDTO.setName("Jose")
        response = restClient.put([path: "/api/users", contentType: "application/json", body:userDTO])

        then:
        response.status == 200
        response.data.name == "Jose"
    }

    def "Check update User with wrong email format"() {
        when:
        PhoneDTO phoneDTO = new PhoneDTO("22223333", "11", "54")
        List<PhoneDTO> listPhoneDto = new ArrayList<>();
        listPhoneDto.add(phoneDTO)
        UserDTO userDTO = new UserDTO("Juan Update", "joseupdate1@gmail.com", "Juan88", listPhoneDto);

        def response = restClient.post([path: "/api/users", contentType: "application/json", body:userDTO])
        userDTO.setId(response.data.id)
        userDTO.setEmail("joseupdate1gmail.com")
        response = restClient.put([path: "/api/users", contentType: "application/json", body:userDTO])

        then:
        HttpResponseException e = thrown(HttpResponseException)
        e.response.status == 400
    }

    def "Check update User with wrong password format"() {
        when:
        PhoneDTO phoneDTO = new PhoneDTO("22223333", "11", "54")
        List<PhoneDTO> listPhoneDto = new ArrayList<>();
        listPhoneDto.add(phoneDTO)
        UserDTO userDTO = new UserDTO("Juan Update", "joseupdate2@gmail.com", "Juan88", listPhoneDto);

        def response = restClient.post([path: "/api/users", contentType: "application/json", body:userDTO])
        userDTO.setId(response.data.id)
        userDTO.setPassword("hola1")
        response = restClient.put([path: "/api/users", contentType: "application/json", body:userDTO])

        then:
        HttpResponseException e = thrown(HttpResponseException)
        e.response.status == 400
    }
}
