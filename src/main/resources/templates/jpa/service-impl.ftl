package ${package}.service.impl;

import ${package}.dto.${pascalName}DTO;
import ${package}.dto.${pascalName}QueryPageDTO;
import ${package}.repository.${pascalName}Repository;
import ${package}.model.${pascalName};
import ${package}.service.${pascalName}Service;
import ${package}.struct.${pascalName}Struct;
import ${package}.vo.${pascalName}VO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ${pascalName}ServiceImpl implements ${pascalName}Service {

    private final ${pascalName}Repository ${camelName}Repository;
    private final ${pascalName}Struct ${camelName}Struct;

    @Override
    public Page<${pascalName}VO> queryPage(${pascalName}QueryPageDTO dto) {
      Pageable pageable = SpecificationBuilder.getPageable(dto.getPageSortInfo());
      Page<${pascalName}> ${camelName}Page = ${camelName}Repository.findAll(pageable);
      return ${camelName}Page.map(${camelName} -> ${camelName}Struct.modelToVO(${camelName}));
    }

    @Override
    public List<${pascalName}VO> queryList(${pascalName}DTO dto) {
      List<${pascalName}> ${camelName}s = SpecificationBuilder.create(${pascalName}Repository.class)
          .select();
      return ${camelName}Struct.modelToVO(${camelName}s);
    }

    @Override
    public ${pascalName}VO get(Long id) {
      Optional<${pascalName}> ${camelName}Optional = ${camelName}Repository.findById(id);
          if (${camelName}Optional.isPresent()) {
          return ${camelName}Struct.modelToVO(${camelName}Optional.get());
      }
      return null;
    }

    @Override
    public ${pascalName}VO add(${pascalName}DTO dto) {
      ${pascalName} ${camelName}= ${camelName}Repository.save(${camelName}Struct.dtoToModel(dto));
      return ${camelName}Struct.modelToVO(${camelName});
    }

    @Override
    public ${pascalName}VO edit(${pascalName}DTO dto) {
      ${pascalName} ${camelName}= ${camelName}Repository.save(${camelName}Struct.dtoToModel(dto));
      return ${camelName}Struct.modelToVO(${camelName});
    }

    @Override
    public void delete(Long id) {
       ${camelName}Repository.deleteById(id);
    }
}