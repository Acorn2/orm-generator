package ${package}.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import ${package}.dto.${pascalName}DTO;
import ${package}.dto.${pascalName}QueryPageDTO;
import ${package}.mapper.${pascalName}Mapper;
import ${package}.model.${pascalName};
import ${package}.service.${pascalName}Service;
import ${package}.vo.${pascalName}VO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ${pascalName}Service {

    private final ${pascalName}Mapper ${camelName}Mapper;
    private final ${pascalName}Struct ${camelName}Struct;

    public Page<${pascalName}VO> queryPage(${pascalName}QueryPageDTO dto) {
      ${pascalName} ${camelName} = ${camelName}Struct.dtoToModel(dto);
      PageHelper.startPage(dto.getPageSortInfo().getPageNum(), dto.getPageSortInfo().getPageSize(), dto.getPageSortInfo().parseSort());
      Page<${pascalName}> ${camelName}Page = (Page<${pascalName}>) ${camelName}Mapper.select(${camelName});
      return PageUtils.convert(${camelName}Page, ${pascalName}VO.class);
    }

    public List<${pascalName}VO> queryList(${pascalName}DTO dto) {
      ${pascalName} ${camelName} = ${camelName}Struct.dtoToModel(dto);
      return ${camelName}Struct.modelToVO(${camelName}Mapper.select(${camelName}));
    }

    public ${pascalName}VO get(Long id) {
      return ${camelName}Struct.modelToVO(${camelName}Mapper.selectByPrimaryKey(id));
    }

    public int add(${pascalName}DTO dto) {
      return ${camelName}Mapper.insertSelective(${camelName}Struct.dtoToModel(dto));
    }

    public int edit(${pascalName}DTO dto) {
      return ${camelName}Mapper.updateByPrimaryKeySelective(${camelName}Struct.dtoToModel(dto));
    }

    public int delete(String id) {
      return ${camelName}Mapper.deleteByPrimaryKey(id);
    }
}