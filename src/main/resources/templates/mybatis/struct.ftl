package ${package}.struct;

import ${package}.model.${pascalName};
import ${package}.dto.${pascalName}DTO;
import ${package}.vo.${pascalName}VO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
  * ${tableComment}转换类.
  */
@Mapper(componentModel = "spring")
public interface ${pascalName}Struct {

    ${pascalName}VO modelToVO(${pascalName} record);

    List<${pascalName}VO> modelToVO(List<${pascalName}> records);

    ${pascalName} voToModel(${pascalName}VO record);

    List<${pascalName}> voToModel(List<${pascalName}VO> records);

    ${pascalName}DTO modelToDTO(${pascalName} record);

    List<${pascalName}DTO> modelToDTO(List<${pascalName}> records);

    ${pascalName} dtoToModel(${pascalName}DTO record);

    List<${pascalName}> dtoToModel(List<${pascalName}DTO> records);
}