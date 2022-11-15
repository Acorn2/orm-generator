package ${package}.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${package}.dto.${pascalName}DTO;
import ${package}.dto.${pascalName}QueryPageDTO;
import ${package}.mapper.${pascalName}Mapper;
import ${package}.model.${pascalName};
import ${package}.service.${pascalName}Service;
import ${package}.struct.${pascalName}Struct;
import ${package}.vo.${pascalName}VO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ${pascalName}ServiceImpl extends ServiceImpl<${pascalName}Mapper, ${pascalName}> implements ${pascalName}Service {

  private final ${pascalName}Struct ${camelName}Struct;

  @Override
  public IPage<${pascalName}VO> queryPage(${pascalName}QueryPageDTO dto) {
    IPage<${pascalName}VO> ${camelName}Page = this.lambdaQuery().page(dto)
        .convert(${camelName} -> ${camelName}Struct.modelToVO(${camelName}));
    return ${camelName}Page;
  }

  @Override
  public List<${pascalName}VO> queryList(${pascalName}DTO dto) {
    List<${pascalName}> ${camelName}List = this.lambdaQuery().list();
    return ${camelName}Struct.modelToVO(${camelName}List);
  }

  @Override
  public ${pascalName}VO get(Long id) {
    return ${camelName}Struct.modelToVO(this.getById(id));
  }

  @Override
  public Boolean add(${pascalName}DTO dto) {
    return this.save(${camelName}Struct.dtoToModel(dto));
  }

  @Override
  public Boolean edit(${pascalName}DTO dto) {
    return this.updateById(${camelName}Struct.dtoToModel(dto));
  }

  @Override
  public Boolean delete(String id) {
    return this.removeById(id);
  }
}