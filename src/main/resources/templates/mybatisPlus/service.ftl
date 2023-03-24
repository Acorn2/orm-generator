package ${package}.service;

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
public class ${pascalName}Service extends
    ServiceImpl<${pascalName}Mapper, ${pascalName}> {

    private final ${pascalName}Struct ${camelName}Struct;

    public IPage<${pascalName}VO> queryPage(${pascalName}QueryPageDTO dto) {
      IPage<${pascalName}VO> ${camelName}Page = this.lambdaQuery().page(dto)
          .convert(${camelName} -> ${camelName}Struct.modelToVO(${camelName}));
      return ${camelName}Page;
    }

    public List<${pascalName}VO> queryList(${pascalName}DTO dto) {
      List<${pascalName}> ${camelName}List = this.lambdaQuery().list();
      return ${camelName}Struct.modelToVO(${camelName}List);
    }

    public ${pascalName}VO get(Long id) {
      return ${camelName}Struct.modelToVO(this.getById(id));
    }

    public boolean add(${pascalName}DTO dto) {
      return this.save(${camelName}Struct.dtoToModel(dto));
    }

    public boolean edit(${pascalName}DTO dto) {
      return this.updateById(${camelName}Struct.dtoToModel(dto));
    }

    public boolean delete(String id) {
      return this.removeById(id);
    }
}