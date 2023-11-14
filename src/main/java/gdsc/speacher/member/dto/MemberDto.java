package gdsc.speacher.member.dto;

import gdsc.speacher.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    private String name;
    private String email;

    public MemberDto(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
    }
}
