package gdsc.speacher.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberEditForm {

    @NotEmpty
    @Email
    private String name;
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
}
