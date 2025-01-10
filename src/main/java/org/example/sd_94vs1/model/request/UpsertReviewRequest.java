package org.example.sd_94vs1.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpsertReviewRequest {
    Integer rating;
    String content;
    String userCode;
}
