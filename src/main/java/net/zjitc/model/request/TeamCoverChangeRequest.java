package net.zjitc.model.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TeamCoverChangeRequest {
    private Long id;
    private MultipartFile file;
}
