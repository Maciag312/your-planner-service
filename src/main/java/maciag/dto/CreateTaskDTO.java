package maciag.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskDTO {

    @ApiModelProperty(position = 0)
    private String name;
    @ApiModelProperty(position = 1)
    private String category;
    @ApiModelProperty(position = 2)
    private Long duration;
    @ApiModelProperty(position = 3)
    private String date;
    @ApiModelProperty(position = 4)
    private boolean isDone;

}
