package gdsc.speacher.dto.member;

import gdsc.speacher.entity.Member;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    private String name;
    private String email;
    private String password;

    public MemberDto(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
        this.password = member.getPassword();
    }
}
